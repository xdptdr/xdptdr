package xpdtr.acme.misc;

import xpdtr.acme.gui.misc.ExceptionType;

public interface ExceptionHandler {

	void handle(Exception e, ExceptionType notFatal);

}
