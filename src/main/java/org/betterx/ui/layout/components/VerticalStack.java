package org.betterx.ui.layout.components;


import org.betterx.ui.layout.components.input.MouseEvent;
import org.betterx.ui.layout.components.render.ComponentRenderer;
import org.betterx.ui.layout.values.Alignment;
import org.betterx.ui.layout.values.DynamicSize;
import org.betterx.ui.layout.values.Rectangle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class VerticalStack<R extends ComponentRenderer> extends Component<R> implements ContainerEventHandler {
    protected final List<Component<?>> components = new LinkedList<>();

    public VerticalStack(DynamicSize width, DynamicSize height) {
        this(width, height, null);
    }

    public VerticalStack(DynamicSize width, DynamicSize height, R renderer) {
        super(width, height, renderer);
    }

    @Override
    protected int updateContainerWidth(int containerWidth) {
        int myWidth = width.calculateOrFill(containerWidth);
        components.stream().forEach(c -> c.width.calculateOrFill(myWidth));
        for (Component<?> c : components) {
            c.updateContainerWidth(c.width.calculatedSize());
        }
        return myWidth;
    }

    @Override
    public int updateContainerHeight(int containerHeight) {
        int myHeight = height.calculateOrFill(containerHeight);
        int fixedHeight = components.stream().map(c -> c.height.calculate(myHeight)).reduce(0, Integer::sum);

        int freeHeight = Math.max(0, myHeight - fixedHeight);
        fillHeight(myHeight, freeHeight);

        for (Component<?> c : components) {
            c.updateContainerHeight(c.height.calculatedSize());
        }

        return myHeight;
    }

    @Override
    void setRelativeBounds(int left, int top) {
        super.setRelativeBounds(left, top);

        int offset = 0;
        for (Component<?> c : components) {
            int delta = relativeBounds.width - c.width.calculatedSize();
            if (c.hAlign == Alignment.MIN) delta = 0;
            else if (c.hAlign == Alignment.CENTER) delta /= 2;
            c.setRelativeBounds(delta, offset);
            offset += c.relativeBounds.height;
        }
    }

    @Override
    public int getContentWidth() {
        return components.stream().map(c -> c.width.calculateFixed()).reduce(0, Integer::max);
    }

    @Override
    public int getContentHeight() {
        int fixedHeight = components.stream().map(c -> c.height.calculateFixed()).reduce(0, Integer::sum);
        double percentage = components.stream().map(c -> c.height.calculateRelative()).reduce(0.0, Double::sum);

        return (int) (fixedHeight / (1 - percentage));
    }

    //    @Override
//    public int fillWidth(int parentSize, int fillSize) {
//        return parentSize;
//    }
    @Override
    public int fillWidth(int parentSize, int fillSize) {
        double totalFillWeight = components.stream().map(c -> c.width.fillWeight()).reduce(0.0, Double::sum);
        return components.stream()
                         .map(c -> c.width.fill(fillSize, totalFillWeight))
                         .reduce(0, Integer::sum);
    }

    @Override
    public int fillHeight(int parentSize, int fillSize) {
        double totalFillHeight = components.stream().map(c -> c.height.fillWeight()).reduce(0.0, Double::sum);
        return components.stream()
                         .map(c -> c.height.fill(fillSize, totalFillHeight))
                         .reduce(0, Integer::sum);
    }

    @Override
    protected void renderInBounds(PoseStack poseStack, Rectangle renderBounds, Rectangle clipRect) {
        super.renderInBounds(poseStack, renderBounds, clipRect);
        for (Component<?> c : components) {
            c.render(poseStack, renderBounds, clipRect);
        }
    }

    @Override
    boolean mouseEvent(MouseEvent event, int x, int y) {
        if (!onMouseEvent(event, x, y)) {
            for (Component<?> c : components) {
                if (c.mouseEvent(event, x - relativeBounds.left, y - relativeBounds.top))
                    return true;
            }
        }
        return false;
    }

    public static VerticalStack<?> centered(Component<?> c) {
        return new VerticalStack<>(DynamicSize.relative(1), DynamicSize.relative(1)).addFiller().add(c).addFiller();
    }

    public static VerticalStack<?> bottom(Component<?> c) {
        return new VerticalStack<>(DynamicSize.relative(1), DynamicSize.relative(1)).add(c).addFiller();
    }

    public VerticalStack<R> add(Component<?> c) {
        this.components.add(c);
        return this;
    }

    private VerticalStack<R> addEmpty(DynamicSize size) {
        this.components.add(new Empty(DynamicSize.fixed(0), size));
        return this;
    }

    public VerticalStack<R> addSpacer(int size) {
        return addEmpty(DynamicSize.fixed(size));
    }

    public VerticalStack<R> addSpacer(float percentage) {
        return addEmpty(DynamicSize.relative(percentage));
    }

    public VerticalStack<R> addFiller() {
        return addEmpty(DynamicSize.fill());
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return components;
    }

    boolean dragging;

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean bl) {
        dragging = bl;
    }

    GuiEventListener focused;

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener) {
        focused = guiEventListener;
    }

//    @Override
//    public void mouseMoved(double x, double y) {
//        for (Component<?> c : components) {
//            c.mouseMoved(x - relativeBounds.left, y - relativeBounds.top);
//        }
//    }
//
//    @Override
//    public boolean mouseClicked(double x, double y, int button) {
//        for (Component<?> c : components) {
//            if (c.mouseClicked(x - relativeBounds.left, y - relativeBounds.top, button))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean mouseReleased(double x, double y, int button) {
//        for (Component<?> c : components) {
//            if (c.mouseReleased(x - relativeBounds.left, y - relativeBounds.top, button))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean mouseDragged(double x, double y, int button, double x2, double y2) {
//        for (Component<?> c : components) {
//            if (c.mouseDragged(
//                    x - relativeBounds.left,
//                    y - relativeBounds.top,
//                    button,
//                    x2 - relativeBounds.left,
//                    y2 - relativeBounds.top
//            ))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean mouseScrolled(double x, double y, double f) {
//        for (Component<?> c : components) {
//            if (c.mouseScrolled(x - relativeBounds.left, y - relativeBounds.top, f))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean keyPressed(int i, int j, int k) {
//        for (Component<?> c : components) {
//            if (c.keyPressed(i, j, k))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean keyReleased(int i, int j, int k) {
//        for (Component<?> c : components) {
//            if (c.keyReleased(i, j, k))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean charTyped(char cc, int i) {
//        for (Component<?> c : components) {
//            if (c.charTyped(cc, i))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean changeFocus(boolean bl) {
//        for (Component<?> c : components) {
//            if (c.changeFocus(bl))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean isMouseOver(double x, double y) {
//        for (Component<?> c : components) {
//            if (c.isMouseOver(x, y))
//                return true;
//        }
//        return false;
//    }
}
