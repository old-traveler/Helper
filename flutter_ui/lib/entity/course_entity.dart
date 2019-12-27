class CourseEntity {
	int code;
	List<CourseData> data;

	CourseEntity({this.code, this.data});

	CourseEntity.fromJson(Map<String, dynamic> json) {
		code = json['code'];
		if (json['data'] != null) {
			data = new List<CourseData>();(json['data'] as List).forEach((v) { data.add(new CourseData.fromJson(v)); });
		}
	}

	Map<String, dynamic> toJson() {
		final Map<String, dynamic> data = new Map<String, dynamic>();
		data['code'] = this.code;
		if (this.data != null) {
      data['data'] =  this.data.map((v) => v.toJson()).toList();
    }
		return data;
	}
}

class CourseData {
	String xqj;
	String jsz;
	String teacher;
	String djj;
	String name;
	String qsz;
	List<int> zs;
	String className;
	int dsz;
	String id;
	String room;

	CourseData({this.xqj, this.jsz, this.teacher, this.djj, this.name, this.qsz, this.zs, this.className, this.dsz, this.id, this.room});

	CourseData.fromJson(Map<String, dynamic> json) {
		xqj = json['xqj'];
		jsz = json['jsz'];
		teacher = json['teacher'];
		djj = json['djj'];
		name = json['name'];
		qsz = json['qsz'];
		zs = json['zs']?.cast<int>();
		className = json['className'];
		dsz = json['dsz'];
		id = json['id'];
		room = json['room'];
	}

	Map<String, dynamic> toJson() {
		final Map<String, dynamic> data = new Map<String, dynamic>();
		data['xqj'] = this.xqj;
		data['jsz'] = this.jsz;
		data['teacher'] = this.teacher;
		data['djj'] = this.djj;
		data['name'] = this.name;
		data['qsz'] = this.qsz;
		data['zs'] = this.zs;
		data['className'] = this.className;
		data['dsz'] = this.dsz;
		data['id'] = this.id;
		data['room'] = this.room;
		return data;
	}
}
