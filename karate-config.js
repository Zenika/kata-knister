function () {

	var env = karate.env;
	
	var config = {
		Url: 'http://localhost:8080',
	};
	

	karate.configure('connectTimeout', 30000);

	karate.configure('readTimeout', 60000);

	return config;

	}
