package javaapplication6;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewMain {

    public static int[][] winningNumbers;
    public static String filePath;
    
    public static void main(String[] args) throws IOException {
        
        filePath = System.getProperty("user.dir") + "\\src\\LOTTO.TXT";
        
        fetchWinningNumbersAndWrite(); 
        
        winningNumbers = fileRead(filePath);
        
        Random rd = new Random();

        int value;
        int p;

        int numOfGames = 10;

        int[] temp = new int[45];
        int[] luckyNumbers = new int[6];
        int[][] billionaire = new int[numOfGames][6];

        int iter = 0;

        for (int i = 0; i < numOfGames; i++) {

            Arrays.fill(temp, 0);

            boolean flag = true;

            while (flag == true) {

                p = 0;

                while (p < 6) {

                    value = rd.nextInt(45) + 1;

                    if (temp[value - 1] == 0) {
                        temp[value - 1] = value;
                        p++;
                    }
                }

                int k = 0;

                for (int e : temp) {
                    if (e != 0) {
                        luckyNumbers[k++] = e;
                    }
                }

                flag = isAlreadyAppeared(luckyNumbers);
            }

            System.arraycopy(luckyNumbers, 0, billionaire[i], 0, 6);
        }

        for (int game[] : billionaire) {
            System.out.println(Arrays.toString(game));
        }

    }

    public static boolean isAlreadyAppeared(int[] arr) {
        boolean flag = false;

        for (int temp[] : winningNumbers) {
            if (Arrays.equals(temp, arr)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static void fetchWinningNumbersAndWrite() throws IOException {
        String URL = "http://nlotto.co.kr/gameResult.do?method=byWin";

        Document doc = Jsoup.connect(URL).get();
        Element target1 = doc.getElementById("desc");
        String pString = target1.attr("content");

        Elements target2 = doc.getElementsByTag("span");
        String date = "";

        for (Element e : target2) {
            if (e.text().contains("2017")) {
                date = e.text();
                break;
            }
        }

        String drawDate = date.substring(1, date.length() - 3).replace(" ", "")
                .replace("년", ".").replace("월", ".").replace("일", "");

        String hitNumbers;
        String bonusNumber;

        int p1, p2, p3, p4, p5;
        String th;

        p1 = pString.indexOf(" ");
        p2 = pString.indexOf(" ", p1 + 1);
        th = pString.substring(p1 + 1, p2 - 1);

        p3 = pString.indexOf(" ", p2 + 1);
        p4 = pString.indexOf("+");
        p5 = pString.indexOf(".");

        hitNumbers = pString.substring(p3 + 1, p4);
        bonusNumber = pString.substring(p4 + 1, p5);

        String fetchedData = th + "," + drawDate + "," + hitNumbers + "," + bonusNumber;

        BufferedReader read= new BufferedReader(new FileReader(filePath));
        ArrayList list = new ArrayList();

        String dataRow = read.readLine(); 
        
        if(!th.equals(dataRow.split(",")[0]))
        {
            while (dataRow != null){
                list.add(dataRow);
                dataRow = read.readLine(); 
            }

            FileWriter writer = new FileWriter(filePath); //same as your file name above so that it will replace it
            writer.append(fetchedData);

            for (int i = 0; i < list.size(); i++){
                writer.append(System.getProperty("line.separator"));
                writer.append(list.get(i).toString());
            }

            writer.flush();
            writer.close();
            System.out.println("The newest winning numbers have been successfully fetched and added to the file.");
            
        }else 
            System.out.println("The newest winning numbers have been already fetched and added to the file.");
        
        System.out.println("");
    }

    public static int[][] fileRead(String str) throws FileNotFoundException, IOException {
        FileReader f = new FileReader(str);
        Scanner scan = new Scanner(f);

        List<List<String>> temp = new ArrayList();

        while (scan.hasNextLine()) {
            temp.add(Arrays.asList(scan.nextLine().split(",")));
        }

        int len = temp.size();

        int[][] data = new int[len][6];

        for (int i = 0; i < len; i++) {
            for (int j = 2; j <= 7; j++) {
                data[i][j - 2] = Integer.parseInt(temp.get(i).get(j));
            }
        }

        f.close();

        return data;

    }
}