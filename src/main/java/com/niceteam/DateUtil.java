package com.niceteam;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

	public static void main(String[] args)throws ParseException {
        
    	String specifiedDay = getToday();
    	System.out.println(specifiedDay);

        System.out.println(getSpecifiedDayBefore(specifiedDay));
        System.out.println(getSpecifiedDayAfter(specifiedDay));
        System.out.println(getFirstDay(specifiedDay));
//        System.out.println(getdaytime("2016-11-12 00:00:00"));
       
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date(System.currentTimeMillis()-86400000*2));
  
		String month = specifiedDay.substring(0,6);
		String dayNum = specifiedDay.substring(6,8);
		System.out.println(month);
		System.out.println(dayNum);
		double time = 1536800200.0;
		System.out.println("时间为:"+timeFormat(time));
		time = 1476394020.000;
		System.out.println(timeFormat(time));
		time = 1476394920.000;
		System.out.println(timeFormat(time));
    }
    
    /**
     * 鑾峰緱褰撳墠鏃ユ湡
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getToday() {//鍙互鐢╪ew Date().toLocalString()浼犻�鍙傛暟
    	Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(date);
        return today;
    }
    
    /**
     * 鑾峰緱鎸囧畾鏃ユ湡鐨勫墠涓�ぉ
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {//鍙互鐢╪ew Date().toLocalString()浼犻�鍙傛暟
        Calendar c = Calendar.getInstance();
       
        try {
        	Date date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
    
        String dayBefore = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return dayBefore;
    }

    /**
     * 鑾峰緱鎸囧畾鏃ユ湡鐨勫悗涓�ぉ
     * 
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
       
        try {
        	 Date date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        	  c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyyMMdd")
                .format(c.getTime());
        return dayAfter;
    }
    
    /**
     * 鑾峰緱鎸囧畾鏃ユ湡鎵�湪鏈堢殑绗竴澶�
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getFirstDay(String specifiedDay) {//鍙互鐢╪ew Date().toLocalString()浼犻�鍙傛暟
   
        return specifiedDay.substring(0,6)+"01";
    }
    
  
    public static int getDayNum(String specifiedDay) {//鍙互鐢╪ew Date().toLocalString()浼犻�鍙傛暟
    	 Calendar c = Calendar.getInstance();
         
    	 try {
    		 
    		 Date date =  new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        
    		 c.setTime(date);
    	 } catch (ParseException e) {
             e.printStackTrace();
         }
    	System.out.println(c.get(Calendar.DAY_OF_MONTH));//鑾峰彇鏄澶氬皯澶�

        return c.get(Calendar.DAY_OF_MONTH);
    }
    
	public static String timeFormat(double time){
//		System.setProperty("user.timezone", "Asia/Shanghai");  
//		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");  
//		TimeZone.setDefault(tz);  
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date((long)time*1000);
		String tim = sdf.format(date);
		return tim;
		
	}
	
	/**
	 * 时间格式化
	 * @param time
	 * @return
	 *//*
	private  String timeFormat(double time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date((long)time*1000);
		String tim = sdf.format(date);
		return tim;
		
	}*/
	
	public static long stringToLong(String strTime, String formatType)	throws ParseException {

 		Date date = stringToDate(strTime, formatType); // String绫诲瀷杞垚date绫诲瀷

 		if (date == null) {

 			return 0;

 		} else {

 			long currentTime = dateToLong(date); // date绫诲瀷杞垚long绫诲瀷

 			return currentTime;

 		}
	}
 		
 		public static Date stringToDate(String strTime, String formatType)	throws ParseException {

 	 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);

 	 		Date date = null;

 	 		date = formatter.parse(strTime);

 	 		return date;

 	 	}

 		
 		public static long dateToLong(Date date)throws ParseException {
 	 		return date.getTime();

 	 	}

 		public static Date longToDate(long currentTime, String formatType)

 	 			throws ParseException {

 	 		Date dateOld = new Date(currentTime); // 鏍规嵁long绫诲瀷鐨勬绉掓暟鐢熷懡涓�釜date绫诲瀷鐨勬椂闂�

 	 		String sDateTime = dateToString(dateOld, formatType); // 鎶奷ate绫诲瀷鐨勬椂闂磋浆鎹负string

 	 		Date date = stringToDate(sDateTime, formatType); // 鎶奡tring绫诲瀷杞崲涓篋ate绫诲瀷

 	 		return date;

 	 	}
 		
 		public static String dateToString(Date data, String formatType) {

 	 		return new SimpleDateFormat(formatType).format(data);

 	 	}

 		/*public static long getdaytime(String date) {
 	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
 	        Date dt2 = null;
 	        try {
 	            dt2 = sdf.parse(date);
 	        } catch (ParseException e) {
 	            // TODO Auto-generated catch block
 	            e.printStackTrace();
 	        }        
 	        return dt2.getTime();
 	    } */
 	
 		public static  String timeToDate(double time) {
 			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	  		//String tim = sdf.format(time);
 	  		Date date = new Date((long)time*1000);
 	  		String tim = sdf.format(date);
 	  
 	  		return tim;
 		}
 	  	
}
