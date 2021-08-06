package org.pdfencrypt.events;

public class ShowViewEventMessage {
    private GuiViewType guiViewType;

    public ShowViewEventMessage(GuiViewType guiViewType) {
        this.guiViewType = guiViewType;
    }

    public GuiViewType getGuiView() {
        return guiViewType;
    }
}
