package capslock.kiddy_register.main;

import methg.commonlib.trivial_logger.Logger;

abstract class ChildController {
    static protected MainController parentController;

    static void setParentController(MainController controller){
        parentController = controller;
    }

    /**
     * 遷移した時,最初に呼び出される.
     */
    void init(){
        Logger.INST.debug( Thread.currentThread().getStackTrace()[1].getMethodName() + " called");
    }

    /**
     * 別の表示状態に遷移するとき,つまり見えなくなるときに呼び出される.
     */
    void transition() {
        Logger.INST.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " called");
    }
}
