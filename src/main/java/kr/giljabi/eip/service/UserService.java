package kr.giljabi.eip.service;

import kr.giljabi.eip.model.User;
import kr.giljabi.eip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User selectOneByUserId(String userId){
        return userRepository.findByUserId(userId);
    }

/*    public boolean existsPhone(String phone){
        return userRepository.existsPhone(phone);
    }

    public boolean existsUserId(String userId){ return userRepository.existsUserId(userId);}*/
/*
    @Transactional
    public int insertUser(InsertUserRequest request, UserPrincipal loginUser){
        Integer shopId = request.getShopId();
        Integer companyId = getCompanyId(shopId);
        Integer agencyId = getAgencyId(companyId);

        User user = User.builder()
                .userId(request.getUserId())
                .userPw(new BCryptPasswordEncoder().encode(request.getPassword()))
                .userNm(request.getUserNm())
                .phoneNo(request.getPhone())
                .agencyId(agencyId)
                .companyId(companyId)
                .shopId(shopId)
                .useFlag(request.getUseFlag())
                .createdBy(loginUser.getUsername()).build();
        userRepository.insertUser(user);

        List<Long> authGroupIdList = request.getAuthGroupIdList();
        int result = this.insertAuthGroupListByUserId(user.getUserId(), authGroupIdList, loginUser);

        return result;
    }

    private Integer getAgencyId(Integer companyId) {
        if(Objects.isNull(companyId)) return null;
        return companyService.findByCompanyId(companyId).getAgencyId();
    }

    private Integer getCompanyId(Integer shopId) {
        if(Objects.isNull(shopId)) return null;
        return shopService.findByShopId(shopId).getCompanyId();
    }

    @Transactional
    public int updateUser(UpdateUserRequest request, UserPrincipal loginUser){
        Integer shopId = request.getShopId();
        Integer companyId = getCompanyId(shopId);
        Integer agencyId = getAgencyId(companyId);

        User.UserBuilder userBuilder = User.builder()
                .userId(request.getUserId())
                .userNm(request.getUserNm())
                .phoneNo(request.getPhone())
                .agencyId(agencyId)
                .companyId(companyId)
                .shopId(shopId)
                .useFlag(request.getUseFlag())
                .updatedBy(loginUser.getUsername());

        if(StringUtils.hasText(request.getPassword())){
            userBuilder.userPw(new BCryptPasswordEncoder().encode(request.getPassword()));
        }

        User user = userBuilder.build();
        userRepository.updateUser(user);

        List<Long> authGroupIdList = request.getAuthGroupIdList();
        int result = this.updateAuthGroupListByUserId(user.getUserId(), authGroupIdList, loginUser);

        return result;
    }

    @Transactional
    public int updateAuthGroupListByUserId(String userId, List<Long> authGroupIdList, UserPrincipal loginUser){

        userRepository.deleteAuthGroupByUserId(userId);

        int result = this.insertAuthGroupListByUserId(userId, authGroupIdList, loginUser);

        return result;
    }

    public int insertAuthGroupListByUserId(String userId, List<Long> authGroupIdList, UserPrincipal loginUser){
        List<UserAuthGroup> userAuthGroupList = new ArrayList<>();

        for(long authGroupId : authGroupIdList){
            UserAuthGroup authGroup = UserAuthGroup.builder()
                    .userId(userId)
                    .authgroupId(authGroupId)
                    .createdBy(loginUser.getUsername()).build();

            userAuthGroupList.add(authGroup);
        }
        int result = userRepository.insertUserAuthGroup(userAuthGroupList);

        return result;
    }

    public List<Long> selectUserAuthGroupIdListByUserId(String userId){
        return userRepository.selectUserAuthGroupIdListByUserId(userId);
    }
*/
}