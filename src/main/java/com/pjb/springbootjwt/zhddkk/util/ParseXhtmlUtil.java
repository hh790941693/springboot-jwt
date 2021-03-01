package com.pjb.springbootjwt.zhddkk.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseXhtmlUtil {

    public static void main(String[] args) {
        try
        {
            Parser parser = Parser.htmlParser();
            parser.settings(new ParseSettings(true, true));
            //Document document = parser.parseInput(new FileReader(new File( "F:/AJWHD0100L.xhtml" )), "");
            Document document = Jsoup.parse( new File( "F:/AJWHD0100L.xhtml" ) , "utf-8" );
            Elements tableEles = document.getElementsByTag("p:dataTable");
            for (Element tableEle : tableEles) {
                System.out.println(tableEles.attr("id"));
                tableEle.removeAttr("headerClass").removeAttr("footerClass").removeAttr("columnClasses");

                if (tableEle.hasAttr("styleClass")) {
                    String styleClassValue = tableEle.attr("styleClass");
                    tableEle.removeAttr("styleClass");
                    tableEle.attr("tableStyleClass", styleClassValue);
                }
                if (tableEle.hasAttr("rowClasses")) {
                    String rowClassessValue = tableEle.attr("rowClasses");
                    tableEle.removeAttr("rowClasses");

                    tableEle.attr("rowIndexVar", "rowIndex");
                    tableEle.attr("rowStyleClass", "#{(rowIndex mod 2) eq 0 ? '" + rowClassessValue.split(",")[0] + "' : '" + rowClassessValue.split(",")[1] + "'}");
                }

                Elements columnGroupEles = tableEle.getElementsByTag("p:columnGroup");
                if (columnGroupEles.size() > 1) {
                    System.out.println("=======================:found more than 1 p:columnGroup");
                    continue;
                }

                List<Element> columnList = new ArrayList<>();
                Element columnGroupEle = columnGroupEles.get(0);
                Elements columnEles = columnGroupEle.getElementsByTag("p:column");
                for (Element columnEle : columnEles) {
                    columnList.add(columnEle);
                }

                // 获取相邻的tbody
                Element siblingEle = tableEle.parent().previousElementSibling();
                Elements tbodyEles = siblingEle.getElementsByTag("tbody");
                if (tbodyEles.size() > 1) {
                    System.out.println("=======================:发现有多个tbody");
                    continue;
                }
                Element tbodyEle = tbodyEles.get(0);

                //移除p:columnGroup
                columnGroupEle.children().remove();
                columnGroupEle.attr("type", "header");

                Elements trElements = tbodyEle.getElementsByTag("tr");
                int trEleCnt = trElements.size();
                if (trEleCnt > 2) {
                    System.out.println("=======================:tbody下发现超过2个以上的tr");
                    continue;
                } else if (trEleCnt == 0) {
                    System.out.println("=======================:tbody下发现0个tr");
                    continue;
                }

                // 设置<p:columnGroup><p:row></p:row>
                for (Element trEle : trElements) {
                    Element pRowElement = columnGroupEle.appendElement("p:row");
                    for (Element thEle : trEle.children()) {
                        Element pColumnElement = pRowElement.appendElement("p:column");
                        // 拷贝p:column属性
                        copyAttrs(thEle, pColumnElement);
                        String headerText = null;
                        try {
                            headerText = findColumnValue(thEle);
                        } catch (Exception e) {
                            continue;
                        }
                        if (null != headerText) {
                            // 设置属性:headerText
                            pColumnElement.attr("headerText", headerText);
                        }
                    }
                }


                if (trEleCnt == 1) {
                    // 直接拷贝
                    for (Element columnEle : columnList) {
                        columnEle.appendTo(tableEle);
                    }
                } else if (trEleCnt == 2) {
                    //设置<p:column>
                    Element tr1Element = trElements.get(0);
                    Element tr2Element = trElements.get(1);
                    int tr1Start=0, tr2Start = tr1Element.children().size();
                    for (Element thEle : tr1Element.children()) {
                        if (thEle.hasAttr("rowspan")) {
                            Element targetElement = columnList.get(tr1Start);
                            targetElement.appendTo(tableEle);
                            tr1Start++;
                            continue;
                        }

                        Element pColumnEle = tableEle.appendElement("p:column");
                        copyAttrs(columnList.get(tr1Start), pColumnEle);
                        Element divUpEle = pColumnEle.appendElement("div");
                        divUpEle.attr("class", "up");

                        Element upElement = columnList.get(tr1Start);
                        divUpEle.html(upElement.html());

                        Element divDownEle = pColumnEle.appendElement("div");
                        divDownEle.attr("class", "down");

                        Element downElement = columnList.get(tr2Start);
                        divDownEle.html(downElement.html());

                        tr1Start++;
                        tr2Start++;
                    }

                    //移除tbody
                    tbodyEle.parent().remove();
                }

                System.out.println(document.html());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findColumnValue(Element pColumnElement) throws Exception {
        String result = null;
        Elements childEles = pColumnElement.children();
        if (childEles.size() > 0) {
            if (childEles.size() == 1) {
                Element firstChildEle = childEles.get(0);
                if (firstChildEle.hasAttr("value")) {
                    result = firstChildEle.attr("value");
                }
            } else {
                throw new Exception("p:column下面发现有多个value属性,请手动确认");
            }
        }

        return result;
    }

    private static void copyAttrs(Element srcEle, Element destEle) {
        Attributes attributes = srcEle.attributes();
        for (Attribute attr : attributes) {
            destEle.attr(attr.getKey(), attr.getValue());
        }
    }

//    private static Element chooseElement(String styleClass, List<Element> elementList) {
//        Element targetElement = null;
//        styleClass.replace("table-header", "");
//        styleClass.replace(" ", "");
//        for (Element ele : elementList) {
//            String classTmp = ele.attr("styleClass");
//            if (styleClass.contains(classTmp)) {
//                targetElement = ele;
//                break;
//            }
//        }
//
//        return targetElement;
//    }
}
