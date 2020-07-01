package com.sxt.sys.vo;

import com.sxt.sys.domain.Dept;
import com.sxt.sys.domain.Notice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeptVo extends Dept {

    private static final long serialVersionUID = 1L;

    private Integer page=1;
    private Integer limit=10;


}
