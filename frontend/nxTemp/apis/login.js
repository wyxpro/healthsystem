import http from '@/nxTemp/config/requestConfig'
import config from "@/nxTemp/config/index.config.js";

/**
 * 登录
 * @param uuid uuid
 * @param cipher 密文
 */
export function loginApi(uname, upw){
	return http.post(`${config.baseUrl}/user/login`, {userNameOrPhone: uname, password: upw});
};

/**
 * 登出
 * @param uuid uuid
 * @param cipher 密文
 */
export function logoutApi(token){
	return http.post(`${config.baseUrl}/api/logout`, { token: token });
};

// 获取公钥
export function getPubKeyApi(uuid) {
	return http.get(`${config.baseUrl}/api/get-pub-key`, { uuid: uuid });
};
