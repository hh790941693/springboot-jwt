package com.pjb.springbootjwt.shop.constants;

/**
 * 超市购物常量.
 */
public class SpConstants {

    /**
     * 订单状态常量.
     */
    public enum OrderStatusEnum {
        WAIT_PAY("待支付", 1),
        PAY_OK("已支付", 2),
        WAIT_DELIVERY("待发货", 3),
        DELIVERY_OK("已发货", 4),
        CANCEL_OK("已取消", 6),
        CONFIRM_OK("已确认收货", 9);


        private String desc;
        private int code;

        OrderStatusEnum(String desc, int code) {
            this.desc = desc;
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
