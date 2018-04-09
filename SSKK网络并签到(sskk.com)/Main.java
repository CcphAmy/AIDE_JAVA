import java.io.*; 
import java.util.*;  
import java.net.HttpURLConnection; 
import java.net.URL; 
import java.net.URLEncoder;

/**
 * @param HTTP GET AND POST FROM http://blog.csdn.net/kimqcn4/article/details/52473085
 */
class MyHttpUrlConn {  
    public static String cookieVal="";


    public static void Get(String url_get,String str_param_url,String charset,String cookie) throws IOException  {  
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码  
        //    String getURL = GET_URL + "?username="  + URLEncoder.encode("fat man", "utf-8");  
        String getURL = url_get + "?" + str_param_url;
        URL getUrl = new URL(getURL);  
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，  
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection  
        HttpURLConnection connection = (HttpURLConnection) getUrl  
			.openConnection();  

        if (cookie != null) {  
            //发送cookie信息上去，以表明自己的身份，否则会被认为没有权限  
            System.out.println("set cookieVal = [" + cookie + "]");
            connection.setRequestProperty("Cookie", cookie);  
        }  

        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到  
        // 服务器  
        connection.connect();  
        // 取得输入流，并使用Reader读取  
        BufferedReader reader = new BufferedReader(new InputStreamReader(  
													   connection.getInputStream(),charset));  
        System.out.println("Contents of get request:");  
        String lines;  
        while ((lines = reader.readLine()) != null)  {  
			System.out.println(lines);  
        }  
        System.out.println(" ");
        reader.close();  
        // 断开连接  
        connection.disconnect();  
    }  
    /**
     * HTTP POST ,RETURN 
     * @param  url_post       [description]
     * @param  str_param_body [description]
     * @param  charset        [description]
     * @param  b_flag         [description]
     * @param  cookies        [description]
     * @return                [COOKIES]
     * @throws IOException    [description]
     * @param 
     * 	2018年3月19日10:07:07 ccph 修复cookies无法多行获取
     */

    public static String Post(String url_post,String str_param_body,String charset,boolean b_flag,String cookies) throws IOException  {  
        // Post请求的url，与get不同的是不需要带参数  
        URL postUrl = new URL(url_post);  
        // 打开连接  
        HttpURLConnection connection = (HttpURLConnection) postUrl  
			.openConnection();  
        // Output to the connection. Default is  
        // false, set to true because post  
        // method must write something to the  
        // connection  
        // 设置是否向connection输出，因为这个是post请求，参数要放在  
        // http正文内，因此需要设为true  
        if("" != cookies){  
            connection.setRequestProperty("Cookie", cookies);  
        }  

        connection.setDoOutput(true);  
        // Read from the connection. Default is true.  
        connection.setDoInput(true);  
        // Set the post method. Default is GET  
        connection.setRequestMethod("POST");  
        // Post cannot use caches  
        // Post 请求不能使用缓存  
        connection.setUseCaches(false);  
        // This method takes effects to  
        // every instances of this class.  
        // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。  
        // connection.setFollowRedirects(true);  

        // This methods only  
        // takes effacts to this  
        // instance.  
        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数  
        connection.setInstanceFollowRedirects(b_flag);  
        // Set the content type to urlencoded,  
        // because we will write  
        // some URL-encoded content to the  
        // connection. Settings above must be set before connect!  
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的  
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode  
        // 进行编码  
        connection.setRequestProperty("Content-Type",  
									  "application/x-www-form-urlencoded");  
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，  
        // 要注意的是connection.getOutputStream会隐含的进行connect。  
        connection.connect();  
        DataOutputStream out = new DataOutputStream(connection  
													.getOutputStream());  
        // The URL-encoded contend  
        // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致  
        //    String content = "userName=" + URLEncoder.encode("console", "utf-8");  
        //    content = content + "&password=" + URLEncoder.encode("12345678", "utf-8");  

        //System.out.println("http param body = [" + str_param_body + "]");
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面  
        out.writeBytes(str_param_body);  

        out.flush();  

        // 取得cookie，相当于记录了身份，供下次访问时使用  
        //    cookieVal = connection.getHeaderField("Set-Cookie").split(";")[0]  
        //cookieVal = connection.getHeaderField("Set-Cookie");
        //修复cookies无法多行获取 ccph 2018年3月19日10
        //
		String key = null;
		String newCookies = "";
		for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
			if (key.equalsIgnoreCase("set-cookie")) {
				cookieVal = connection.getHeaderField(i);
				cookieVal = cookieVal.substring(0,cookieVal.indexOf(";"));
				newCookies = newCookies + cookieVal + ";";
			}
		}

        System.out.println("GET COOKIES = [" + newCookies  + "]");

        out.close(); // flush and close  
        BufferedReader reader = new BufferedReader(new InputStreamReader(  
													   connection.getInputStream(),charset));  
        String line;  
        System.out.println("Contents of post request:");  
        while ((line = reader.readLine()) != null)  {  
            System.out.println(line);  
        }  
        System.out.println(" ");

        reader.close();  
        connection.disconnect();  

        return newCookies;
    }  
}  

public class Main{

	/**
	 * 登录SSKK网络并签到
	 * Sign in https://www.sskk.ml
	 * 作者:ccph
	 * 时间：2018年3月19日09:42:44
	 * @param args [null]
	 */
	public static void main(String[] args) {
		final String URL_LOGIN = "https://www.sskk.ml/auth/login"; //POST /auth/login HTTP/1.1
		final String URL_SIGNIN ="https://www.sskk.ml/user/checkin"; //POST /user/checkin HTTP/1.1 
		String loginMaill ="";
		String loginPass  =""; 


		/**
		 * 		Login {"ret":1,"msg":"\u6b22\u8fce\u56de\u6765"}
		 * 		error 
		 */		try{
			String cookieLogin = MyHttpUrlConn.Post(URL_LOGIN,"email=" + loginMaill + "&code&passwd=" + loginPass,"utf-8",false,""); //login
			MyHttpUrlConn.Post(URL_SIGNIN,"","utf-8",false,cookieLogin);//sign in
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
}
