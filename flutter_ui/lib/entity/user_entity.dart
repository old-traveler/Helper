class UserEntity {
	int code;
	UserData data;
	String rememberCodeApp;
	String msg;

	UserEntity({this.code, this.msg ,this.data, this.rememberCodeApp});

	UserEntity.fromJson(Map<String, dynamic> json) {
		code = json['code'];
		msg = json['msg'];
		data = json['data'] != null ? new UserData.fromJson(json['data']) : null;
		rememberCodeApp = json['remember_code_app'];
	}

	Map<String, dynamic> toJson() {
		final Map<String, dynamic> data = new Map<String, dynamic>();
		data['code'] = this.code;
		data['msg'] = this.msg;
		if (this.data != null) {
      data['data'] = this.data.toJson();
    }
		data['remember_code_app'] = this.rememberCodeApp;
		return data;
	}
}

class UserData {
	String address;
	String rememberCodeApp;
	String headPic;
	String active;
	String bio;
	String depName;
	String headPicThumb;
	int userId;
	String school;
	String lastUse;
	String trueName;
	String className;
	String studentKH;
	String username;

	UserData({this.address, this.rememberCodeApp, this.headPic, this.active, this.bio, this.depName, this.headPicThumb, this.userId, this.school, this.lastUse, this.trueName, this.className, this.studentKH, this.username});

	UserData.fromJson(Map<String, dynamic> json) {
		address = json['address'];
		rememberCodeApp = json['remember_code_app'];
		headPic = json['head_pic'];
		active = json['active'];
		bio = json['bio'];
		depName = json['dep_name'];
		headPicThumb = json['head_pic_thumb'];
		userId = json['user_id'];
		school = json['school'];
		lastUse = json['last_use'];
		trueName = json['TrueName'];
		className = json['class_name'];
		studentKH = json['studentKH'];
		username = json['username'];
	}

	Map<String, dynamic> toJson() {
		final Map<String, dynamic> data = new Map<String, dynamic>();
		data['address'] = this.address;
		data['remember_code_app'] = this.rememberCodeApp;
		data['head_pic'] = this.headPic;
		data['active'] = this.active;
		data['bio'] = this.bio;
		data['dep_name'] = this.depName;
		data['head_pic_thumb'] = this.headPicThumb;
		data['user_id'] = this.userId;
		data['school'] = this.school;
		data['last_use'] = this.lastUse;
		data['TrueName'] = this.trueName;
		data['class_name'] = this.className;
		data['studentKH'] = this.studentKH;
		data['username'] = this.username;
		return data;
	}
}
