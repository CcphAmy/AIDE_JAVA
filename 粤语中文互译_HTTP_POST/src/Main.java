import java.net.URLDecoder;
import java.net.URLEncoder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;  

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;  
import java.util.List;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  


//response not json,
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

public class Main{
    /**
     * 
     * @param url
     * @param param
     * @return
     */
    public static String HttpPost(String url, String param) {
        String result="";
        PrintWriter output=null;
        BufferedReader input=null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
									"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            output = new PrintWriter(conn.getOutputStream());

            output.print(param);
            output.flush();

            input = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("print error ,Exception!"+e);
            e.printStackTrace();
        }
        //finally close 
        finally{
            try{
                if(output!=null){
                    output.close();
                }
                if(input!=null){
                    input.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /** 
     * write file stream
     * @param
     * @param
     * @return 
     * from web...
     */  
	public static void writeByFileOutputStream(String content,String filePath) {  

        FileOutputStream fop = null;  
        File file;
        try {  
            file = new File(filePath);  
            fop = new FileOutputStream(file);  
            // if file doesnt exists, then create it  
            if (!file.exists()) {  
                file.createNewFile();  
            }  
            // get the content in bytes  
            byte[] contentInBytes = content.getBytes();  

            fop.write(contentInBytes);  
            fop.flush();  
            fop.close();  

        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (fop != null) {  
                    fop.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  

    /** 
     * return one string 
     * @param
     * @param
     * @return 
     */  
    public static String getSubUtilSimple(String soap,String rgex){  
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);  
        while(m.find()){  
            return m.group(1);  
        }  
        return "";  
    }  

    /** 
     * Unicode to chinese
     * @param
     * @param
     * @return 
     * form http://blog.csdn.net/u010612373/article/details/51441161
     */ 
	public static String decodeUnicode(final String dataStr) {   
		int start = 0;   
		int end = 0;   
		final StringBuffer buffer = new StringBuffer();   
		while (start > -1) {   
			end = dataStr.indexOf("\\u", start + 2);   
			String charStr = "";   
			if (end == -1) {   
				charStr = dataStr.substring(start + 2, dataStr.length());   
			} else {   
				charStr = dataStr.substring(start + 2, end);   
			}   
			char letter = (char) Integer.parseInt(charStr, 16); // 16 parseInt   
			buffer.append(new Character(letter).toString());   
			start = end;   
		}   
		return buffer.toString();   
	}

	/** 
     * Send baidufanyi post
     * @param postData
     * @return void
     */

	public static void sendBaiduPost(String[] postData,int typeParam){

		String urlStr ="";

		for (String strParam:postData) {//
			//System.out.println(StrParam);
			//String urlStr=URLEncoder.encode(postText, "gbk");
			try{
				//urlStr = URLEncoder.encode(strParam, "ISO-8859-1");
				urlStr = URLEncoder.encode(strParam, "utf-8");
                String from="yue",to="zh";
                if(typeParam==2){from=new String("zh");to=new String("yue");}
				urlStr ="query=" + urlStr + "&from="+from+"&to="+to;
				//System.out.println("Param--->" + urlStr);
				String str = HttpPost("http://fanyi.baidu.com/basetrans",urlStr);
				//System.out.println(str);
				System.out.printf("%s \n\t--> \n\t\t%s\n",strParam,decodeUnicode(getSubUtilSimple(str,"\"dst\":\"(.*?)\"")));  

			}catch (UnsupportedEncodingException e) {
				System.out.println("UnsupportedEncodingException error!");
				e.printStackTrace();  
			} 
		}
	}

    public static void main(String[] args) {
		//Scanner
		Scanner inScan=new Scanner(System.in);
		System.out.print("粤语翻译中文1, 中文翻译粤语2:");
		int inTypeParam=inScan.nextInt();
		if (inTypeParam!=2) {inTypeParam=1;}
		System.out.print("Input string:");
		String inScanStr=inScan.next();
		String []inScanStrArray;
		while(inScanStr.indexOf("exit")==-1){
			inScanStrArray=new String[]{inScanStr};
			sendBaiduPost(inScanStrArray,inTypeParam);
			System.out.print("Input string or exit:");
			inScanStr=inScan.next();
		}
		System.out.println("App close!");
	}
}
