package com.veeva.automation.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacketDataUtils {

    public static  File saveJacketData(List<Map<String, String>> allProducts) throws IOException {
    	System.out.println("I am inside saveJacketData utility methods");
        File file = new File("target/JacketData.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map<String, String> item : allProducts) {
                writer.write(item.get("Title") + " | " + item.get("Price") + " | Top Seller: " + item.get("TopSeller"));
                writer.newLine();
            }
        }
        return file;
    }
}

