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
	Screen parent;

	SwitchButton enabledButton;
	SwitchButton sneakToggleButton;
	SimpleSlider tickDelaySlider;
	ButtonWidget doneButton;

	public ConfigScreen(Screen parent) {
		super(Text.empty());
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		int buttonWidth = 48;
		int buttonHeight = 18;
		int panelWidth = 256;

		enabledButton = new SwitchButton(
				(width / 2) + (panelWidth / 2) - (buttonWidth), (height / 2) - (buttonHeight * 2),
				buttonWidth, buttonHeight, ReplanterPlus.enabled) {
			@Override
			public void setToggled(boolean toggled) {
				super.setToggled(toggled);
				ReplanterPlus.enabled = toggled;
			}
		};
		addDrawableChild(enabledButton);
		addSelectableChild(enabledButton);
		TextWidget t = new TextWidget(Text.translatable("replanter.configscreen.enabled"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
				enabledButton.getY() + (buttonHeight / 2) - (textRenderer.fontHeight / 2));
		addDrawableChild(t);

		sneakToggleButton = new SwitchButton(
				enabledButton.getX(), enabledButton.getY() + enabledButton.getHeight(),
				enabledButton.getWidth(), enabledButton.getHeight(),
				ReplanterPlus.sneakToggle) {
			@Override
			public void setToggled(boolean toggled) {
				super.setToggled(toggled);
				ReplanterPlus.sneakToggle = toggled;
			}
		};
		addDrawableChild(sneakToggleButton);
		addSelectableChild(sneakToggleButton);
		t = new TextWidget(Text.translatable("replanter.configscreen.sneakToggle"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
			sneakToggleButton.getY() + (buttonHeight / 2) - (textRenderer.fontHeight / 2));
		addDrawableChild(t);

		t = new TextWidget(Text.translatable("replanter.configscreen.tickDelay"), textRenderer);
		t.setPosition((width / 2) - (panelWidth / 2),
			sneakToggleButton.getY() + buttonHeight);
		addDrawableChild(t);

		tickDelaySlider = new SimpleSlider(0, 8);
		tickDelaySlider.setPosition(t.getX(), t.getY() + t.getHeight());
		tickDelaySlider.setWidth(panelWidth);
		tickDelaySlider.setHeight(24);
		tickDelaySlider.setIValue(ReplanterPlus.useDelay);
		tickDelaySlider.onValue = (Long l) -> 
		{
			long i = l;
			ReplanterPlus.useDelay = (int)i;
		};
		addDrawableChild(tickDelaySlider);
		addSelectableChild(tickDelaySlider);

		doneButton = ButtonWidget.builder(Text.translatable("replanter.configscreen.done"), (ButtonWidget b) -> {
			close();
		})
				.size(96, 24)
				.position((width / 2) - (96 / 2), tickDelaySlider.getY() + tickDelaySlider.getHeight() + 8)
				.build();
		addDrawableChild(doneButton);
		addSelectableChild(doneButton);
	}

	public static class SimpleSlider extends SliderWidget {
		long min, max;
		long iValue;
		public Consumer<Long> onValue;

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
			iValue = (long) Math.round(value * (max - min)) + min;
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

	public class SwitchButton extends ToggleButtonWidget {
		private static final ButtonTextures TEXTURES = new ButtonTextures(new Identifier("widget/button"),
				new Identifier("widget/button"), new Identifier("widget/button_highlighted"));

		public SwitchButton(int x, int y, int width, int height, boolean toggled) {
			super(x, y, width, height, toggled);
			setTextures(TEXTURES);
		}

		@Override
		protected boolean clicked(double mouseX, double mouseY) {
			if (super.clicked(mouseX, mouseY)) {
				setToggled(!toggled);
				return true;
			}
			return false;
		}

		@Override
		public void render(DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			context.drawCenteredTextWithShadow(textRenderer,
					Text.translatable("replanter.switchbutton.label." + (toggled ? "on" : "off")),
					getX() + (width / 2), getY() + (height / 2) - (textRenderer.fontHeight / 2),
					toggled ? 0x00ff00 : 0xff0000);
		}
	}

	@Override
	public void close() {
		client.setScreen(parent);
		ReplanterPlus.saveConfig();
	}
}
