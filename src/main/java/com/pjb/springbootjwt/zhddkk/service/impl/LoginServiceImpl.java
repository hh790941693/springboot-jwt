package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomInfoDTO;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomUsersDTO;
import com.pjb.springbootjwt.zhddkk.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private WsChatroomUsersServiceImpl wsChatroomUsersService;

    @Override
    public WsChatroomInfoDTO getChatRoomInfo(String roomId) {
        WsChatroomInfoDTO wciDTO = new WsChatroomInfoDTO();
        wciDTO.setCyyList(buildCommonData("cyy"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 房间所有用户列表
        List<WsChatroomUsersDTO> chatroomAllUserList = wsChatroomUsersService.queryChatroomUserList(roomId);
        for (WsChatroomUsersDTO allUser : chatroomAllUserList) {
            allUser.setUpdateTimeStr(sdf.format(allUser.getUpdateTime()));
        }
        // 房间在线用户列表
        List<WsChatroomUsersDTO> onlineUserList = chatroomAllUserList.stream().filter(userObj->userObj.getStatus().intValue() == 1).collect(Collectors.toList());
        // 房间管理员用户
        List<WsChatroomUsersDTO> managerUserList = chatroomAllUserList.stream().filter(userObj->userObj.getIsManager().intValue() == 1).collect(Collectors.toList());

        wciDTO.setChatroomAllUserList(chatroomAllUserList);
        wciDTO.setChatroomOnlineUserList(onlineUserList);
        // 房间管理员用户列表
        wciDTO.setManagerUserList(managerUserList);
        // 在线人数
        wciDTO.setRoomUserCount(onlineUserList.size());

        return wciDTO;
    }

    /**
     * 构造常用语对象.
     * @return list
     */
    private List<WsCommonDO> buildCommonData(String type) {
        Map<String, List<WsCommonDO>> commonMap = new HashMap<>();
        List<WsCommonDO> commonList = CoreCache.getInstance().getCommonList();
        return commonList.stream().filter(obj->obj.getType().equals(type)).collect(Collectors.toList());
    }
}
