package com.sport365.badminton.entity.resbody;

import java.io.Serializable;
import java.util.ArrayList;

import com.sport365.badminton.entity.obj.HomeKeyWordObj;
import com.sport365.badminton.entity.obj.NearActiveObj;
import com.sport365.badminton.entity.obj.SportAdvertismentObj;
import com.sport365.badminton.entity.obj.SportThemeObj;

/**
 * GetSprotHome首页返回接口
 */
public class GetSprotHomeResBody implements Serializable {

	private static final long serialVersionUID = 1L;

	/*"body": {
        "homeImg": "首页中部广告",
        "homeImgUrl": "http://www.17workout.com/Admin/UploadFile/api/middleAdv.png",
        "sportAdvertismentList": [
            {
                "noticeUrlType": "",
                "redirectUrl": "",
                "imageName": "广告1",
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/test1.png",
                "tag": "",
                "versionBegin": 0,
                "versionEnd": 0,
                "category": 0,
                "provinceId": "",
                "cityId": "",
                "sortIndex": 0,
                "isTop": 0
            },
            {
                "noticeUrlType": "",
                "redirectUrl": "",
                "imageName": "广告1",
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/test1.png",
                "tag": "",
                "versionBegin": 0,
                "versionEnd": 0,
                "category": 0,
                "provinceId": "",
                "cityId": "",
                "sortIndex": 0,
                "isTop": 0
            },
            {
                "noticeUrlType": "",
                "redirectUrl": "",
                "imageName": "广告1",
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/test1.png",
                "tag": "",
                "versionBegin": 0,
                "versionEnd": 0,
                "category": 0,
                "provinceId": "",
                "cityId": "",
                "sortIndex": 0,
                "isTop": 0
            }
        ],
        "sportThemeList": [
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/zt1.png",
                "title": "我身边",
                "urlType": "0",
                "jumpUrl": ""
            },
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/zt2.png",
                "title": "运动日历",
                "urlType": "0",
                "jumpUrl": ""
            },
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/zt3.png",
                "title": "抽奖",
                "urlType": "0",
                "jumpUrl": ""
            },
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/zt4.png",
                "title": "期待更懂",
                "urlType": "0",
                "jumpUrl": ""
            }
        ],
        "homeKeyWordsList": [
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/venue.png",
                "title": "运动会所",
                "urlType": "0",
                "jumpUrl": "http://www.17workout.com/Admin/UploadFile/api/venue.png"
            },
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/club.png",
                "title": "俱乐部",
                "urlType": "0",
                "jumpUrl": "http://www.17workout.com/Admin/UploadFile/api/club.png"
            },
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/active.png",
                "title": "活动",
                "urlType": "0",
                "jumpUrl": "http://www.17workout.com/Admin/UploadFile/api/active.png"
            },
            {
                "imageUrl": "http://www.17workout.com/Admin/UploadFile/api/match.png",
                "title": " 比赛",
                "urlType": "0",
                "jumpUrl": "http://www.17workout.com/Admin/UploadFile/api/match.png"
            }
        ],
        "nearActiveList": [
            {
                "clubId": "",
                "clubName": "",
                "payStyle": "1",
                "memberId": "",
                "memberName": "",
                "isSpecial": "1",
                "activeId": "414",
                "provinceId": "17",
                "provinceName": "江苏",
                "cityId": "220",
                "cityName": "苏州",
                "countyId": "2149",
                "countyName": "吴中区",
                "venueId": "11",
                "venueName": "苏化科技园",
                "topNum": "",
                "realNum": "",
                "createUesr": "",
                "createUesrName": "",
                "modifyUserId": "",
                "modifyUserName": "",
                "activeTitle": "0111+周日+苏化科技园(娄葑)",
                "activeDate": "2015-1-11 19:00:00",
                "activeHours": "2",
                "activeFee": "25",
                "weekDay": "周五",
                "createTime": "",
                "endTime": "",
                "modifyTime": "",
                "isValid": "",
                "isDel": "",
                "code": "544365",
                "mobile": "13052892875",
                "remark": ""
            }
        ],
        "hotActiveList": [],
        "isCache": "0",
        "aboutUs": "精羽门是致力于给广大运动爱好者提供在线活动报名,赛事报名、赛事组织以及教练、陪练、羽毛球俱乐部信息发布等功能的专业性的电子商务平台。<br>精羽门倡导一起锻炼，一起快乐的健康体育休闲文化。我们希望通过网站平台能为大众的需求提供优质的服务和便捷的信息渠道，激发大众对各项体育活动的热情，营造一个全民参与的健康活跃的氛围。",
        "contactUs": "赛事组织:尹先生 13052892875 <br> 俱乐部入驻: NC 13052892875"
    }*/
	public String homeImg;
	public String homeImgUrl;//底部广告1个固定
	public ArrayList<SportAdvertismentObj> sportAdvertismentList = new ArrayList<SportAdvertismentObj>();// 首页顶部广告
	public ArrayList<SportThemeObj> sportThemeList = new ArrayList<SportThemeObj>();
	public ArrayList<HomeKeyWordObj> homeKeyWordsList = new ArrayList<HomeKeyWordObj>();//导航菜单，4个
	public ArrayList<NearActiveObj> nearActiveList = new ArrayList<NearActiveObj>();//废弃
	public ArrayList<NearActiveObj> hotActiveList = new ArrayList<NearActiveObj>();//废弃
	public String isCache;
	public String aboutUs;//关于我们
	public String contactUs;// 联系我们
}
