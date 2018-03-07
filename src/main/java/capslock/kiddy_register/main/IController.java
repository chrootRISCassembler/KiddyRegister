package capslock.kiddy_register.main;


import methg.commonlib.trivial_logger.Logger;

public interface IController {

    /**
     * 遷移した時,最初に呼び出される.
     */
    default void init(){
        Logger.INST.debug("Default method IController#init called");
    }
}
