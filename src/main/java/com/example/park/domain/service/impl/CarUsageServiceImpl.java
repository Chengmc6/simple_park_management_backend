package com.example.park.domain.service.impl;

import com.example.park.common.PageResult;
import com.example.park.domain.dto.SimpleUserDTO;
import com.example.park.domain.dto.UsageRequestDTO;
import com.example.park.domain.dto.UsageResponseVO;
import com.example.park.domain.entity.Car;
import com.example.park.domain.entity.CarUsage;
import com.example.park.domain.mapper.CarMapper;
import com.example.park.domain.mapper.CarUsageMapper;
import com.example.park.domain.mapper.UserMapper;
import com.example.park.domain.service.ICarUsageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 高明(コウメイ)
 * @since 2025-10-15
 */
@Service
public class CarUsageServiceImpl extends ServiceImpl<CarUsageMapper, CarUsage> implements ICarUsageService {

    
    private final CarUsageMapper carUsageMapper;

    
    private final UserMapper userMapper;

    
    private final CarMapper carMapper;

    public CarUsageServiceImpl(CarUsageMapper carUsageMapper,UserMapper userMapper,CarMapper carMapper){
        this.carUsageMapper=carUsageMapper;
        this.userMapper=userMapper;
        this.carMapper=carMapper;
    }
    /**
     * 查询指定车辆的使用履历（分页）
     * 指定された車両の使用履歴をページングして取得する
     */
    @Override
    public PageResult<UsageResponseVO> history(UsageRequestDTO dto) {
        
        // 1. 构造分页对象（页码 + 每页条数）
        // ページングオブジェクトを作成（ページ番号＋ページサイズ）
        Page<CarUsage> page=new Page<>(dto.getPageNum(),dto.getPageSize());

        // 2. 查询该车辆的使用履历记录，按乘车时间倒序排列
        // 指定車両の使用履歴を取得、乗車時間で降順に並べる
        Page<CarUsage> usagePage=carUsageMapper.selectPage(page, new QueryWrapper<CarUsage>()
            .eq("car_id",dto.getCarId())// 按车辆 ID 筛选 ／ 車両IDで絞り込み
            .orderByDesc("ride_time")// 按乘车时间倒序 ／ 乗車時間で降順
        );

        // 3. 提取所有使用记录中的用户 ID（去重）
        // 使用履歴からユーザーIDを抽出（重複排除）
        List<Long> ids=usagePage.getRecords().stream()
            .map(CarUsage::getUserId)
            .distinct()
            .collect(Collectors.toList());
        
            //ユーザー名を抽出
        Map<Long,String> userMap=userMapper.selectByIds(ids).stream()
            .collect(Collectors.toMap(SimpleUserDTO::getId, SimpleUserDTO::getUsername));
        
            // 4. 查询车辆信息（只查一次）
        // 車両情報を一度だけ取得
        Car car=carMapper.selectById(dto.getCarId());

        // 5. 拼装视图对象列表（UsageResponseVO）
        // 表示用オブジェクト（UsageResponseVO）を組み立てる
        List<UsageResponseVO> voList=usagePage.getRecords().stream().map(
            record -> {
                // 将 CarUsage 的字段复制到 VO
                // CarUsage のフィールドを VO にコピー
                UsageResponseVO vo=new UsageResponseVO();
                BeanUtils.copyProperties(record, vo);

                // 补充车辆信息（车牌号）
                // 車両情報（ナンバー）を補完
                if(car !=null){
                    vo.setCarNumber(car.getCarNumber());
                }

                // 补充用户名
                // ユーザー名を補完
                vo.setUsername(userMap.get(record.getUserId()));
                return vo;
            }
        ).collect(Collectors.toList());

        // 6. 封装分页结果返回
        // ページング結果をまとめて返却
        PageResult<UsageResponseVO> result=new PageResult<>(usagePage.getTotal(),
            usagePage.getCurrent(),
            usagePage.getSize(),
            usagePage.getPages(),
            voList);

        return result;
    }

}
