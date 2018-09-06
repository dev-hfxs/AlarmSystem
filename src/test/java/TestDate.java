import java.util.Date;

import com.sierotech.alarmsys.common.utils.DateUtils;

/**
* <p>版权所有:(C)2018-2022 天津航峰希萨科技有限公司 </p>
* @创建人: lwm
* @创建日期: 2018年8月27日
* @修改人: 
* @修改日期：
* @描述: 
 */

/**
* @JDK版本: 1.7
* @创建人: lwm
* @创建日期：2018年8月27日
* @功能描述: 
 */
public class TestDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 String curYear = "" + DateUtils.getYear();
		 String strDate1 = curYear + "-01-01";
		 
		 System.out.println(strDate1);

		 int curMonth = DateUtils.getMonth();
		 for(int i=1; i<curMonth; i++) {
			 String monthBeginDay = curYear +"-"+ i+"-01";
			 if(i< 10) {
				 monthBeginDay = curYear + "-0"+i+"-01";
			 }
			 
			 int nextMonth = i + 1;
			 String nextMonthBeginDay = curYear +"-" + nextMonth + "-01";
			 if(nextMonth< 10) {
				 nextMonthBeginDay = curYear + "-0" + nextMonth + "-01";
			 }
			 if(nextMonth == 13) {
				 int nextYear = DateUtils.getYear() + 1;
				 nextMonthBeginDay = nextYear + "-01-01";
			 }
			 System.out.println("startDate:"+monthBeginDay +",endDate:"+nextMonthBeginDay);
		 }
		 
	}

}
