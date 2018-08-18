
package com.fayayo.job.manager.core.cluster.ha;

import com.fayayo.job.common.enums.ResultEnum;
import com.fayayo.job.common.exception.CommonException;

public  class HaStrategyFactory {

    public static HaStrategy createHaStrategy(Integer ha){

        if(ha==HaStrategyEnums.FAIL_FAST.getCode()){

            return new FailfastHaStrategy();

        }else if(ha==HaStrategyEnums.FAIL_OVER.getCode()){

            return new FailoverHaStrategy();

        }else {
            throw new CommonException(ResultEnum.HA_NOT_EXIST);
        }
    }
}
