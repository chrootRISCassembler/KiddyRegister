package capslock.kiddy_register.main;

import methg.commonlib.trivial_logger.Logger;

public class WriteController extends ChildController{


    @Override
    public final void init() {
        Logger.INST.debug("WriteController#init called");

        MainHandler.INST.writeToJSON();
    }
}
