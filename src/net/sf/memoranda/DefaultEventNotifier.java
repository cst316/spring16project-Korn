/**
 * DefaultEventNotifier.java Created on 10.03.2003, 21:18:02 Alex Package:
 * net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003
 * Memoranda Team. http://memoranda.sf.net
 */
package net.sf.memoranda;

import net.sf.memoranda.ui.EventNotificationDialog;

/**
 *
 */
/*$Id: DefaultEventNotifier.java,v 1.4 2004/01/30 12:17:41 alexeya Exp $*/
public class DefaultEventNotifier implements EventNotificationListener {

    /**
     * Constructor for DefaultEventNotifier.
     */
    public DefaultEventNotifier() {
        super();
    }

    /**
     * @see net.sf.memoranda.EventNotificationListener#eventIsOccured(net.sf.memoranda.Event)
     */
    public void eventIsOccured(Event ev) {
        new EventNotificationDialog(
                "Memoranda event",
                ev.getTimeString(),
                ev.getText());
    }

    /**
     * @see net.sf.memoranda.EventNotificationListener#eventsChanged()
     */
    public void eventsChanged() {
        //
    }


}
