package com.lhh.vista.common.util;

public class StateTool {
    public static void checkState(boolean expression, int state) {
        if (!expression) {
            throw new StateException(state);
        }
    }

    public static class StateException extends RuntimeException {
        private int state;

        public StateException(int state) {
            super();
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public class State {
        public static final int SUCCESS = 1;//统一的成功返回代码
        public static final int FAIL = -1;//统一的失败返回代码
        public static final int BLANCE_ERR = 1000;//统一的失败返回代码


        public static final int ORDER_ITEM_ERROR = -1000;
        public static final int ORDER_SESSION_ERROR = -1001;
        public static final int ORDER_MEMMBER_ERROR = -1002;
        public static final int ORDER_MEMMBER_TICKET_ERROR = -1003;
        public static final int ORDER_SEAT_CAN_NOT_USE_ERROR = -1004;
        public static final int ORDER_SEAT_ERROR = -1005;
        public static final int ORDER_ADD_TICKET_ERROR = -1006;
        public static final int ORDER_PIN_ERROR = -1007;


        public static final int REWARD_CISHUBUZU = -1001;
    }
}
