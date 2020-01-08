import java.util.Collections;
import java.util.Scanner;
import marsrover.adapter.MarsRoverAdapter;
import marsrover.adapter.dto.CoordinateValue;
import marsrover.adapter.dto.InitialMarsRoverValues;
import marsrover.adapter.dto.MapSizeValue;
import marsrover.api.commandhandler.DefaultMarsRoverCommandHandler;
import marsrover.api.queryhandler.GetMarsRoverQueryHandler;
import marsrover.domain.repo.MarsRoverRepository;
import marsrover.infra.repo.InMemoryRepository;

public class MarsRover
{
    private static MarsRoverRepository repo = new InMemoryRepository();

    private static MarsRoverAdapter marsRoverAdapter = new MarsRoverAdapter(
        new DefaultMarsRoverCommandHandler(repo),
        new GetMarsRoverQueryHandler(repo));

    public static void main(String[] args)
    {

        Scanner reader = new Scanner(System.in);
        System.out.println("Insert horizontal map size:");
        int sizex = reader.nextInt();
        System.out.println("Insert vertical map size:");
        int sizey = reader.nextInt();

        System.out.println("Insert horizontal initial rover position:");
        int roverx = reader.nextInt();
        System.out.println("Insert vertical initial rover position:");
        int rovery = reader.nextInt();
        System.out.println("Insert initial rover direction:");
        String roverz = reader.next(); //n = north, e = east, w = west, s = south

        marsRoverAdapter.initializeMarsRover(new InitialMarsRoverValues(
            new MapSizeValue(sizex, sizey), new CoordinateValue(roverx, rovery), roverz, Collections.emptyList()
        ));
        do
        {
            System.out.println("Insert command (f = forward, b = backward, l = turn left, r = turn right):");

            String command = reader.next();

            marsRoverAdapter.moveMarsRover(command);
            System.out.println(marsRoverAdapter.getMarsRovers());
        } while (true);
    }


}
