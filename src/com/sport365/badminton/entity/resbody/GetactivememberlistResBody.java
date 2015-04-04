package com.sport365.badminton.entity.resbody;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 报名列表中的数据返回
 * 
 * @author Frank
 * 
 */
public class GetactivememberlistResBody implements Serializable {
	/*{"response":{"header":{"rspType":"0","rspCode":"0000","rspDesc":"查询成功"},"body":
	{"activeRegistList":
		[{"nickName":"菁英-小福-121","activeId":"4314","memberId":"b8e101e817011c6c531d48ae4945a14e",
			"createDate":"2015-4-3 11:13:02","isValid":"1","isDel":"0"}]}}}*/
	public ArrayList<MemberInfo> activeRegistList = new ArrayList<MemberInfo>(); 
	
	public class MemberInfo implements Serializable{
		public String nickName;
		public String activeId;
		public String memberId;
		public String createDate;
		public String isValid;
		public String isDel;
	}
}
