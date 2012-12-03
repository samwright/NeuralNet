package neural;

import equation.Equation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 12:10
 */
public class NeuroneImpl implements Neurone {
    private List<Link> input_links = new LinkedList<Link>();
    private List<Link> output_links = new LinkedList<Link>();
    private final Equation transition_equation;
    private double value, bias = 0, weighted_total;


    public NeuroneImpl(Equation transition_equation) {
        this.transition_equation = transition_equation;
    }

    @Override
    public List<Link> getInputLinks() {
        return Collections.unmodifiableList(input_links);
    }

    @Override
    public List<Link> getOutputLinks() {
        return Collections.unmodifiableList(output_links);
    }

    @Override
    public void addInputLink(Link link) {
        input_links.add(link);
    }

    @Override
    public void addOutputLink(Link link) {
        output_links.add(link);
    }

    @Override
    public void removeInputLink(Link link) {
        input_links.remove(link);
    }

    @Override
    public void removeOutputLink(Link link) {
        output_links.remove(link);
    }

    @Override
    public void fire() {
        weighted_total = bias;

        for (Link link : input_links) {
            weighted_total += link.getWeight() * link.getInput().getValue();
        }

        value = transition_equation.evaluate(weighted_total);
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public double getWeightedInput() {
        return weighted_total;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
