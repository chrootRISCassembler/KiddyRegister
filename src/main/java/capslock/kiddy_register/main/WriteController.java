package capslock.kiddy_register.main;

import methg.commonlib.trivial_logger.Logger;

public class WriteController implements IController {


    @Override
    public final void init() {
        Logger.INST.debug("Write init called");

        MainHandler.INST.writeToJSON();
    }
}
