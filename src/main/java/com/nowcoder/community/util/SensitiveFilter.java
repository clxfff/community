package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author clx
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);


    /**
     * 替换符
     */
    private static final String REPLACEMENT = "***";

    /**
     * 根节点
     */
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    private void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词文件失败！" + e.getMessage());
        }
    }

    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {

            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            // 指向子节点，进入下一次循环
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length()-1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        // 指针1
        TrieNode tempNode = rootNode;

        // 指针2
        int begin = 0;

        // 指针3
        int position = 0;

        StringBuilder stringBuilder = new StringBuilder();

        while (position < text.length()) {

            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {

                // 若指针1处于根节点，将此符号记入结果，指针2走下一步
                if (tempNode == rootNode) {
                    stringBuilder.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            // 检查下一个节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                stringBuilder.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd) {
                // 发现敏感词，将begin-position的字符串替换
                stringBuilder.append(REPLACEMENT);
                begin = ++position;
                // 进入下一个位置
                tempNode = rootNode;
            } else {
                position++;
            }
        }

        // 将最后一批字符计入结果
        stringBuilder.append(text.substring(begin));

        return stringBuilder.toString();
    }

    private boolean isSymbol(Character c) {
        // 2e80-9fff是东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2e80 || c > 0x9fff);
    }

    /**
     * 前缀树
     */
    private class TrieNode {

        // 关键字结束标识符
        private boolean isKeywordEnd = false;

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        private Map<Character, TrieNode> subNotes = new HashMap<>(16);

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNotes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNotes.get(c);
        }
    }


}
