package ReadFileData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFiledata {

    public static String[][] readArray_String(String address) {


        FileReader reader = null;

        BufferedReader readerBuf = null;

        String[][] array = null;
        try {

            reader = new FileReader(address);

            readerBuf = new BufferedReader(reader);

            List<String> strList = new ArrayList<>();

            String lineStr;

            while((lineStr = readerBuf.readLine()) != null) {

                strList.add(lineStr);
            }

            int lineNum = strList.size();

            String s =  strList.get(0);
            int columnNum = s.split("\\*\\*").length;

            array = new String[strList.size()][columnNum];

            int count = 0;

            for(String str : strList) {

                String[] strs = str.split("\\*\\*");
                for(int i = 0; i < columnNum; i++) {
                    array[count][i] = strs[i];
                }

                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(readerBuf != null)
                    readerBuf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return array;
    }
}


