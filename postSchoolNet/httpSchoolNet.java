import java.net.URLDecoder;
import java.net.URLEncoder;

import java.io.*;
import java.net.URL;
import java.net.InetAddress;
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
//
//



//�Զ����쳣��
class MyException extends Exception {
    String message;
    MyException(String param){
        message =new String(param);
    }
    public String getString(){
        return message;
    }
}

//������

public class httpSchoolNet{
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
									"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.3538.400 QQBrowser/9.6.12501.400");

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
     * GET
     * @param url
     * @param param
     * @return
     */
    public static String HttpGet(String url,String params)
    {
        String result="";
        //System.out.println("into");
        BufferedReader in=null;
        try {
            String urlName=params.equals("")?url:url+"?"+params;
            URL realUrl;
            realUrl = new URL(urlName);
            URLConnection conn=realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.3538.400 QQBrowser/9.6.12501.400");
            conn.connect();
            Map<String,List<String>> map=conn.getHeaderFields();
            for(String key:map.keySet())
            {
                System.out.println(key+"--->"+map.get(key));
            }
            in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line=in.readLine())!=null)
            {
                result+="\n"+line;
            }
        } 
        catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("GET error"+e);
            e.printStackTrace();
        }
        finally
        {
            if(in!=null)
            {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
     * Send postTwoDianXin
     * @param postData
     * @return void
     */

    private static void postTwoDianXin(String userid,String passwd) throws Exception {  
        InetAddress ia = InetAddress.getLocalHost(); 
        String localIp = ia.getHostAddress();  //ip
        System.out.println("getHostAddress--->[" + localIp + "]");
        if (localIp.equals("")) {
            MyException myExc = new MyException("ip address error");
            throw myExc;
        }
        String reTwoGet =HttpGet("http://219.136.125.139/portalReceiveAction.do?wlanacname=pyzyxy-2&wlanuserip="+localIp,"");
        //wlanacIp" value='1
        //type="hidden" name="(.*?)"
        //
        String wlanacIp=new String(getSubUtilSimple(reTwoGet,"wlanacIp\" value=\'(.*?)\'"));
        System.out.println("wlanacIp--->[" +wlanacIp+"]");
        //ֻ֧�ֵ��ţ���ͨ��Ҫ�����޸�
        String postParams = new String("wlanuserip=" +localIp +"&wlanacname=pyzyxy-2&chal_id=&chal_vector=&auth_type=PAP&seq_id=&req_id=&wlanacIp="+
        wlanacIp+"&ssid=&vlan=&mac=&message=&bank_acct=&isCookies=&version=0&authkey=pyzyxy&url=&usertime=0&listpasscode=0&listgetpass=0"+
        "&getpasstype=0&randstr=&domain=PYZYXY.GZ&isRadiusProxy=false&usertype=0&isHaveNotice=0&times=12&weizhi=0&smsid=0&freeuser="+
        "&freepasswd=&listwxauth=0&templatetype=1&tname=5&logintype=0&act=&is189=true&terminalType=&useridtemp="+userid+"&userid="+userid+
        "@PYZYXY.GZ&passwd="+passwd);
        //System.out.println("postParams--->"+postParams);
        //189 ����
        String reTwoPost = HttpPost("http://219.136.125.139/portalAuthAction.do",postParams);
/*        byte[] bs = reTwoPost.getBytes("GBK");//utf-8
        reTwoPost = new String(bs,"UTF-8");*/
        //System.out.println("reTwoPost--->"+reTwoPost);
        if (reTwoPost.indexOf(userid +"@PYZYXY.GZ")>-1) {
            System.out.println("login--->[True]");
            String message =new String(getSubUtilSimple(reTwoPost,"message\" value=\'(.*?)\'"));
            System.out.println("message--->["+message+"]");
        }else{System.out.println("login--->[False]");}
        // System.out.println("getHostAddress--->" +reTwoGet);
    }  

    public static void main(String[] args)  {
        String userId="1713440101";//id
        String password ="";//one
        String passwordTwo ="";//two
        String service ="dianxin";//dianxin
        try{

            Scanner inScan=new Scanner(System.in);
/*            System.out.print("please int:");
            int inTypeParam=inScan.nextInt();*/
            //��¼
            String reTest = HttpGet("http://192.168.200.84","");
            //System.out.println(reTest);
            if (reTest.indexOf("���ѳɹ�����У԰��")==-1) {//�Ҳ���

                if(reTest.indexOf("top.self.location.href")>-1){
                    //ҳ����ת ��һ��ҳ�� post 
                    //

                    String postDataT=new String(getSubUtilSimple(reTest,"jsp\\?(.*?)\'</scr"));
                    postDataT = postDataT.replace("=","%253D"); postDataT = postDataT.replace("&","%2526");//%253D = ��%2526 &

                    String postParams = "userId=" + userId+"&password=" + password + "&service=" + service + "&queryString=" +
                                        postDataT + "&operatorPwd=&operatorUserId=&validcode=";

                    //System.out.println("postParams--->"+postParams);
                    //��һ��ҳ���¼
                    String reOnePost = HttpPost("http://192.168.200.84/eportal/InterFace.do?method=login",postParams);

                    byte[] bs = reOnePost.getBytes("GBK");//utf-8
                    reOnePost = new String(bs,"UTF-8");

                    String result =new String(getSubUtilSimple(reOnePost,"result\":\"(.*?)\""));
                    String message =new String(getSubUtilSimple(reOnePost,"message\":\"(.*?)\""));
                    System.out.println("result--->["+result+"]");
                    System.out.println("message--->["+message+"]");
                    if (result.indexOf("success")>-1) {
                        postTwoDianXin(userId,passwordTwo);
                    }
                }

            }else{
                //�׳�
                postTwoDianXin(userId,passwordTwo);
                }

        }catch (MyException e) {
            System.out.println("Exception--->error 1000");
            //e.printStackTrace();
        }catch (Exception ea) {
            System.out.println("Exception--->error 1001");
            //ea.printStackTrace();
        }
	}
}