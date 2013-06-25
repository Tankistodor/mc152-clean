package CustomOreGen;

import CustomOreGen.Util.GeometryStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;

public class GeometryData extends GeometryRequestData
{
    public transient Collection geometry;
    private static final long serialVersionUID = 2L;

    public GeometryData() {}

    public GeometryData(GeometryRequestData var1, Collection var2)
    {
        super(var1.world, var1.chunkX, var1.chunkZ, var1.batchID);
        this.geometry = var2;
    }

    private void writeObject(ObjectOutputStream var1) throws IOException
    {
        GeometryStream.getStreamData(this.geometry, var1);
    }

    private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException
    {
        this.geometry = Arrays.asList(new GeometryStream[] {new GeometryStream(var1)});
    }
}
