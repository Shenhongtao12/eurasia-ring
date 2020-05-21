package com.sht.eurasiaring;

import com.sht.eurasiaring.entity.User;
import com.sht.eurasiaring.utils.JwtUtils;
import org.junit.jupiter.api.Test;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 17:20
 **/
public class test1 {
    @Test
    void aVoid(){
        System.out.println("https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIx2K2AlX1MvPurrdmz99cNWqSMASxbjyr3uaFyB9goct9Picd545g2ibSkbIgZQFcetdnfBUs2UkoA/132".length());
    }

    @Test
    void token(){
        User user = new User();
        user.setId(2);
        user.setNickName("Binary");
        user.setOpenid("otwpb5HTpiMlSH7EQ6r5Ezr7nNQw");
        user.setSession_key("aaaaaaaaaa");
        System.out.println(JwtUtils.geneJsonWebToken(user));
    }
}
