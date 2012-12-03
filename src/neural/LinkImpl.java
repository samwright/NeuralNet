package neural;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 12:17
 */
public class LinkImpl implements Link {
    private final Neurone input, output;
    double weight;

    public LinkImpl(Neurone input, Neurone output) {
        this.input = input;
        this.output = output;

        input.addOutputLink(this);
        output.addInputLink(this);
    }

    @Override
    public Neurone getInput() {
        return input;
    }

    @Override
    public Neurone getOutput() {
        return output;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void delete() {
        input.removeOutputLink(this);
        output.removeInputLink(this);
    }
}
