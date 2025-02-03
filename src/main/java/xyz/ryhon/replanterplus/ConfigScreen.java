package xyz.ryhon.replanterplus;

import java.util.function.Consumer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigScreen extends Screen {
	private Screen parent;

	public ConfigScreen(Screen parent){
		super(Text.empty());
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		int buttonWidth = 48;
		int buttonHeight = 18;
		int panelWidth = 256;

		SwitchButton enabledButton = new SwitchButton(
				(width / 2) + (panelWidth / 2) - (buttonWidth), (height / 2) - (buttonHeight * 4),
				buttonWidth, buttonHeight, ReplanterPlus.CONFIG.isEnabled()) {
			@Override
			public void setToggled(boolean toggled) {
				super.setToggled(toggled);
				ReplanterPlus.CONFIG.setEnabled(toggled);
			}
		};
		addDrawableChild(enabledButton);
		addSelectableChild(enabledButton);
		TextWidget t = new TextWidget(Text.translatable("replanter.configscreen.enabled"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
				enabledButton.getY() + (buttonHeight / 2) - (textRenderer.fontHeight / 2));
		addDrawableChild(t);

		SwitchButton sneakToggleButton = new SwitchButton(
				enabledButton.getX(), enabledButton.getY() + enabledButton.getHeight(),
				enabledButton.getWidth(), enabledButton.getHeight(),
				ReplanterPlus.CONFIG.isSneakToggle()) {
			@Override
			public void setToggled(boolean toggled) {
				super.setToggled(toggled);
				ReplanterPlus.CONFIG.setSneakToggle(toggled);
			}
		};
		addDrawableChild(sneakToggleButton);
		addSelectableChild(sneakToggleButton);
		t = new TextWidget(Text.translatable("replanter.configscreen.sneakToggle"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
				sneakToggleButton.getY() + (buttonHeight / 2) - (textRenderer.fontHeight / 2));
		addDrawableChild(t);

		SwitchButton missingItemNotificationsButton = new SwitchButton(
				sneakToggleButton.getX(), sneakToggleButton.getY() + sneakToggleButton.getHeight(),
				sneakToggleButton.getWidth(), sneakToggleButton.getHeight(),
				ReplanterPlus.CONFIG.isMissingItemNotifications()) {
			@Override
			public void setToggled(boolean toggled) {
				super.setToggled(toggled);
				ReplanterPlus.CONFIG.setMissingItemNotifications(toggled);
			}
		};
		addDrawableChild(missingItemNotificationsButton);
		addSelectableChild(missingItemNotificationsButton);
		t = new TextWidget(Text.translatable("replanter.configscreen.missingItemNotifications"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
				missingItemNotificationsButton.getY() + (buttonHeight / 2) - (textRenderer.fontHeight / 2));
		addDrawableChild(t);

		SwitchButton autoSwitchButton = new SwitchButton(
				missingItemNotificationsButton.getX(),
				missingItemNotificationsButton.getY() + missingItemNotificationsButton.getHeight(),
				missingItemNotificationsButton.getWidth(), missingItemNotificationsButton.getHeight(),
				ReplanterPlus.CONFIG.isAutoSwitch()) {
			@Override
			public void setToggled(boolean toggled) {
				super.setToggled(toggled);
				ReplanterPlus.CONFIG.setAutoSwitch(toggled);
			}
		};
		addDrawableChild(autoSwitchButton);
		addSelectableChild(autoSwitchButton);
		t = new TextWidget(Text.translatable("replanter.configscreen.autoSwitch"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
				autoSwitchButton.getY() + (buttonHeight / 2) - (textRenderer.fontHeight / 2));
		addDrawableChild(t);

		SwitchButton requireSeedHeldButton = new SwitchButton(
				autoSwitchButton.getX(),
				autoSwitchButton.getY() + autoSwitchButton.getHeight(),
				autoSwitchButton.getWidth(), autoSwitchButton.getHeight(),
				ReplanterPlus.CONFIG.isRequireSeedHeld()) {
			@Override
			public void setToggled(boolean toggled) {
				super.setToggled(toggled);
				ReplanterPlus.CONFIG.setRequireSeedHeld(toggled);
			}
		};
		addDrawableChild(requireSeedHeldButton);
		addSelectableChild(requireSeedHeldButton);
		t = new TextWidget(Text.translatable("replanter.configscreen.requireSeedHeld"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
				requireSeedHeldButton.getY() + (buttonHeight / 2) - (textRenderer.fontHeight / 2));
		addDrawableChild(t);

		t = new TextWidget(Text.translatable("replanter.configscreen.tickDelay"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
				requireSeedHeldButton.getY() + buttonHeight);
		addDrawableChild(t);

		SimpleSlider tickDelaySlider = new SimpleSlider(0, 8);
		tickDelaySlider.setPosition(t.getX(), t.getY() + t.getHeight());
		tickDelaySlider.setWidth(panelWidth);
		tickDelaySlider.setHeight(24);
		tickDelaySlider.setIValue(ReplanterPlus.CONFIG.getUseDelay());
		tickDelaySlider.onValue = (Long l) -> ReplanterPlus.CONFIG.setUseDelay(l.intValue());
		addDrawableChild(tickDelaySlider);
		addSelectableChild(tickDelaySlider);

		ButtonWidget doneButton = ButtonWidget.builder(Text.translatable("replanter.configscreen.done"), (ButtonWidget b) -> close())
				.size(96, 24)
				.position((width / 2) - (96 / 2), tickDelaySlider.getY() + tickDelaySlider.getHeight() + 8)
				.build();
		addDrawableChild(doneButton);
		addSelectableChild(doneButton);
	}

	private class SimpleSlider extends SliderWidget {
		long min;
		long max;
		long iValue;
		private Consumer<Long> onValue;

		public SimpleSlider(long min, long max) {
			super(0, 0, 0, 0, Text.empty(), 0);
			this.min = min;
			this.max = max;
			updateMessage();
		}

		public void setIValue(long v) {
			iValue = v;
			setValue((v - min) / (double) (max - min));
		}

		@Override
		protected void applyValue() {
			iValue = Math.round(value * (max - min)) + min;
			setIValue(iValue);

			updateMessage();
			if (onValue != null)
				onValue.accept(iValue);
		}

		@Override
		protected void updateMessage() {
			setMessage(Text.literal(iValue + " / " + max));
		}
	}

	private class SwitchButton extends ToggleButtonWidget {
		private static final ButtonTextures SWITCH_TEXTURES = new ButtonTextures(Identifier.of("widget/button"),
				Identifier.of("widget/button"), Identifier.of("widget/button_highlighted"));

		public SwitchButton(int x, int y, int width, int height, boolean toggled) {
			super(x, y, width, height, toggled);
			setTextures(SWITCH_TEXTURES);
		}

		@Override
		public void onClick(double mouseX, double mouseY) {
			super.onClick(mouseX, mouseY);
			setToggled(!toggled);
		}

		@Override
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			super.renderWidget(context, mouseX, mouseY, delta);
			context.drawCenteredTextWithShadow(textRenderer,
					Text.translatable("replanter.switchbutton.label." + (toggled ? "on" : "off")),
					getX() + (width / 2), getY() + (height / 2) - (textRenderer.fontHeight / 2),
					toggled ? 0x00ff00 : 0xff0000);
		}
	}

	@Override
	public void close() {
		client.setScreen(parent);
		ReplanterPlus.CONFIG.save();
	}
}
