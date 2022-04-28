package com.example.oversell.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.security.auth.PrivateCredentialPermission;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Items {
    private String id;
    private String itemName;
    private Integer catId;
    private Integer rootCatId;
    private Integer sellCounts;
    private Integer onOffStatus;
    private Date createdTime;
    private Date updatedTime;
    private String content;
}
