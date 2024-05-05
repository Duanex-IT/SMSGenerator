package com.usb.sms.generator.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DmpTransactionsMapper {

    @Select("select * from usb_stagetransactions")
    public List<Object> getTransactions();

    @Insert("usb_stagetransactions_out_processed_status")
    public void addProcessedSign();

}
