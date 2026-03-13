package com.nwpu.healthsystem.backend.mapper;

import com.nwpu.healthsystem.backend.entity.UserHealthInfo;
import org.springframework.stereotype.Repository;


@Repository
public interface UserHealthInfoMapper {
    /**
     * 根据用户id 获取用户健康信息
     *@param userId
     *@return
     */
    UserHealthInfo getUserHealthInfoByUserId(long userId);

    /**
     * 插入用户健康信息，    无法插入 用户的标注信息，这一部分信息只提供给 医生记录
     *@param userHealthInfo
     *@return
     */
    int insertUserHealthInfo(UserHealthInfo userHealthInfo);

    /**
     * 修改用户健康信息     无法更新 医生id（注册时初始化）和  用户的标注信息，这一部分信息只提供给 医生记录
     *@param userHealthInfo
     *@return
     */
    int updateUserHealthInfo(UserHealthInfo userHealthInfo);

//    待扩展 医生 更新用户标注信息、
    long getDoctorIdofUser(long userId);

    /**
     * 修改用户健康信息 （专门添加 用户的标注信息）    无法更新 医生id（注册时初始化）
     *@param userHealthInfo
     *@return
     */
    int updateUserHealthInfoByDoctor(UserHealthInfo userHealthInfo);

    /**
     * 修改 抑郁标注
     *@param
     *@return
     */
    int updateDepressedInfoByDoctor(long userId, String depressed, String remark);

}
