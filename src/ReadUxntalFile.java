import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ReadUxntalFile {

    public static void main(String[] args) {
        ReadUxntalFile readUxntalFile = new ReadUxntalFile();
        List<String> list = readUxntalFile.readUnxtal();
        Map<String, List<String>> map = readUxntalFile.splitBlock(list);
        System.out.println("Here is the result:");
        if (map != null) {
            for (String s : map.keySet()) {
                System.out.println(s + "   " + map.get(s));
            }
        }
    }


    //    Read from a .tal file and store it
    public List<String> readUnxtal() {
        File file = new File(".\\Uxntal\\");
        File[] files = file.listFiles();
        System.out.println("Here are the names of all the Uxntal files.");
        for (File file2 : files) {
            //打印文件列表：只读取名称使用getName();
//            System.out.println("Path："+file2.getPath());
            System.out.println("File name：" + file2.getName());
        }
        System.out.println("Please enter the name of the Uxntal file that you want to compile:");
        Scanner sc = new Scanner(System.in);
        String fileName = "./Uxntal/" + sc.next() + ".tal";
        System.out.println("Program start to read " + fileName + "by word.");
        List<String> list = new ArrayList<>();
        String string = null;

        try {
            string = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println("Error: The file is not exist.");
            return null;
        }

        string = clearComment(string);

        //Clear newline characters
        string = string.replaceAll("\\n", " ");
        string = string.replaceAll("\\r", " ");
        // Clear blank
        String[] arr = string.split("\s+");


        for (String str : arr) {
            String trimed = str.trim();
            if (!trimed.equals("")) {
                list.add(trimed);
            }
        }

        // print
        System.out.println("The content of the file: ");
        list.stream().forEach(System.out::println);
        return list;
    }


    // Clear comment
    public String clearComment(String content) {
        content = content.trim();
        int left = content.indexOf("(");

        // If there is no "(", then return
        if (left == -1) {
            return content;
        } else {
            int right = left + 1;
            int count = 1;
            while (left != -1 && (left + 1) < content.length()) {
                // Clear comments like: ( ( comment ) )
                if (content.charAt(right) == '(') {
                    count++;
                } else if (content.charAt(right) == ')') {
                    count--;
                }
                right++;
                //Clear comment
                if (count == 0) {
                    String comment = content.substring(left, right);
                    content = content.replace(comment, "");
                    //Any more comment?
                    left = content.indexOf('(');
                    if (left != -1) {
                        right = left + 1;
                        count = 1;
                    }
                }
            }
        }
        return content;
    }


    // Split the code into blocks
    public Map<String, List<String>> splitBlock(List<String> list) {

        if (list == null) {
            System.out.println("Error: There is no such list.");
            return null;
        }

        Map<String, List<String>> map = new LinkedHashMap<>();

        int i = 0;

//        zero page
        List<String> zeroPage = new ArrayList<>();
        while (i < list.size()) {
            String str = list.get(i);
            if (str.contains("|0100")) {
                break;
            }
            zeroPage.add(str);
            i++;
        }
        map.put("Zero Page", zeroPage);

//       main program
        List<String> mainProgram = new ArrayList<>();
        while (i < list.size()) {
            String str = list.get(i);
            if (str.contains("BRK")) {
                mainProgram.add(str);
                break;
            }
            i++;
            mainProgram.add(str);
        }
        map.put("Main Program", mainProgram);


//        i++;
//        while (i < list.size()) {
////            function
//            int functionCount = 0;
//            if (list.get(i).contains("@")) {
//                List<String> function = new ArrayList<>();
//                functionCount++;
//                while (i < list.size()) {
//                    String str = list.get(i);
//                    if (str.contains("JMP2") || str.contains("JMP2r")) {
//                        function.add(str);
//                        break;
//                    }
//                    i++;
//                    function.add(str);
//                }
//                map.put("function" + functionCount, function);
//                i++;
//                continue;
//            }
////            label
//            if (list.get(i).contains("&")) {
//                List<String> label = new ArrayList<>();
//                while (i < list.size()) {
//                    String str = list.get(i);
//                    label.add(str);
//                    i++;
//                    break;
//                }
//                map.put("Label", label);
//            }
//            i++;
//        }

        i++;
        int count = 0;
        while (i < list.size() && list.get(i).contains("@")) {
            count++;
            List<String> function = new ArrayList<>();
            while (i < list.size()) {
                String str = list.get(i);
                if (str.contains("JMP2") || str.contains("JMP2r")) {
                    function.add(str);
                    break;
                }
                i++;
                function.add(str);
            }
            map.put("Function " + count, function);
            i++;
        }

//        //
//        i++;
//        if (i < list.size() && list.get(i).contains("@")) {
//            List<String> function = new ArrayList<>();
//            while (i < list.size()) {
//                String str = list.get(i);
//                if (str.contains("&")) {
//                    function.add(str);
//                    break;
//                }
//                i++;
//                function.add(str);
//            }
//            map.put("Function ", function);
//        }

        // global variable
        i++;
        List<String> label = new ArrayList<>();
        while (i < list.size()) {
            String str = list.get(i);
            label.add(str);
            i++;
        }
        map.put("Label", label);

        System.out.println();
        return map;
    }
}

