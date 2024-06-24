import java.util.*;

// Player class representing individual players
class Player {
    String name;
    int ballTaken = 0;
    int runScored = 0;
    int ballBowled = 0;
    int runsConcealed = 0;
    int wicketTaken = 0;
    boolean out = false;
}

// Team class representing a cricket team
class Team {
    String name;
    int runs = 0;
    int wickets = 0;
    int n;
    Player[] players;

    Team(int n) {
        this.n = n;
        players = new Player[n];
        for (int i = 0; i < n; i++) {
            players[i] = new Player();
        }
    }
}

public class CricketMatchSimulation {
    private static Player bowler = new Player();
    private static Player batter = new Player();
    private static Team[] teams = new Team[2];
    private static int overs;

    private static Scanner in = new Scanner(System.in);

    // Method to add runs based on user input
    static void addruns(int run, Team team, String option) {
        switch (option) {
            case "wicket":
                bowler.wicketTaken += 1;
                team.wickets += 1;
                batter.ballTaken += 1;
                bowler.ballBowled += 1;
                batter.out = true;
                break;
            case "runout":
                team.runs += run;
                team.wickets += 1;
                batter.ballTaken += 1;
                bowler.runsConcealed += run;
                batter.runScored += run;
                batter.out = true;
                break;
            case "nb":
            case "wide":
                team.runs += run;
                bowler.runsConcealed += run;
                if (teams[1].runs > teams[0].runs)
                    break;
                // prompting again for input as we get 1 more ball
                run = in.nextInt();
                getbatter(team);
                option = in.next().toLowerCase();
                addruns(run, team, option);
                break;

            case "n":
                batter.runScored += run;
                batter.ballTaken += 1;
                bowler.ballBowled += 1;
                bowler.runsConcealed += run;
                team.runs += run;
                break;
            default:
                System.out.println("Invalid option. No runs added.");
                break;
        }

    }

    // method to take team names ,player names ,total no.of overs to be played
    private static void additional_info() {
        System.out.println("\nEnter no.of Total overs to be played in single innings:");
        overs = in.nextInt();
        System.out.println("Enter no. of players in each team");
        int nplayers = in.nextInt();

        teams[0] = new Team(nplayers);
        teams[1] = new Team(nplayers);

        for (int i = 0; i < 2; i++) {
            if (i == 0)
                System.out.printf("Enter Team %d name or which team is batting first: ", i + 1);
            if (i == 1)
                System.out.printf("Enter Team %d name  ", i + 1);
            teams[i].name = in.next();
            System.out.printf("Enter Player names for team %d : ", i + 1);
            for (int j = 0; j < teams[i].n; j++) {
                teams[i].players[j].name = in.next();
            }
        }

    }

    private static void getbatter(Team team) {
        String batterName = in.next();
        for (Player player : team.players) {
            if (batterName.equals(player.name) && !player.out) {
                batter = player;
                return;
            }
        }
        System.out.println("Invalid batter name.Enter again");
        getbatter(team);

    }

    private static void getBowler(int i) {
        String bowlerName = in.next();
        for (Player player : teams[i].players) {

            if (bowlerName.equals(player.name)) {
                bowler = player;
                return;
            }
        }
        System.out.println("Invalid bowler name. Enter again.");
        getBowler(i);
    }

    private static void result(Team teamA, Team teamB) {
        System.out.println();
        if (teamA.runs > teamB.runs) {
            System.out.println(teamA.name + " WON !");
        } else if (teamA.runs < teamB.runs) {
            System.out.println(teamB.name + " WON !");
        } else {
            System.out.println("Match tied!");
        }
    }

    private static void Summary() {
        for (int i = 0; i < 2; i++) {
            Team team = teams[i];
            System.out.println("\nTeam: " + team.name);

            System.out.println("Total Runs: " + teams[i].runs + ", Total Wickets: " + teams[i].wickets);

            // Display batsmen details in table format
            System.out.println("Batters:");
            System.out.printf("%-20s | %-12s | %-15s | %-12s%n", "Name", "Runs Scored", "Balls Faced", "Strike rate");
            System.out.println("-------------------------------------------------------------");
            for (Player player : team.players) {
                System.out.printf("%-20s | %-12d | %-15d | %-12.2f%n",
                        player.name, player.runScored, player.ballTaken, calculateStrikeRate(player));
            }
            System.out.println();

            // Display bowlers details in table format
            System.out.println("Bowlers:");
            System.out.printf("%-20s | %-12s | %-15s | %-12s%n", "Name", "Runs Conceded", "Wickets Taken",
                    "Economy Rate");
            System.out.println("-------------------------------------------------------------");
            for (Player player : team.players) {
                if (player.ballBowled > 0) {
                    System.out.printf("%-20s | %-12d | %-15d | %-12.2f%n",
                            player.name, player.runsConcealed, player.wicketTaken, calculateEconomyRate(player));
                }
            }

            System.out.println(); // Add a blank line between teams for clarity
        }

        System.out.printf("%s : %d / %d    vs      \"%s : %d / %d  ", teams[0].name, teams[0].runs, teams[0].wickets,
                teams[0].name, teams[0].runs, teams[0].wickets);

    }

    // Helper method to calculate strike rate
    private static float calculateStrikeRate(Player player) {
        return (float) player.runScored / player.ballTaken * 100.0f;
    }

    // Helper method to calculate economy rate
    private static float calculateEconomyRate(Player player) {
        return (float) player.runsConcealed / (player.ballBowled / 6.0f); // Assuming 6 balls per over
    }

    private static void stimulateMatch() {
        int run;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < overs; j++) {
                System.out.println("Enter new bowler name :");
                int t = (i == 0) ? 1 : 0;
                getBowler(t);

                for (int k = 1; k <= 6; k++) {
                    System.out.printf("%d.%d :", j, k);
                    run = in.nextInt();
                    getbatter(teams[i]);
                    String delivery = in.next().toLowerCase();
                    addruns(run, teams[i], delivery);
                    if (teams[i].wickets == teams[i].n - 1 || teams[1].runs > teams[0].runs)
                        break;
                }
                System.out.printf("%s: %d/ %d  After %d overs\n", teams[i].name, teams[i].runs, teams[i].wickets,
                        j + 1);
            }
            if (i == 0) {
                System.out.println("\nFinal Score :");
                System.out.printf("%s : %d / %d\n", teams[i].name, teams[i].runs, teams[i].wickets);
                System.out.printf("\n%s needs %d runs in %d Overs to win !\n", teams[i + 1].name, teams[i].runs + 1,
                        overs);
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++)
            System.out.print("*");
        ;
        System.out.println("\n              Welcome !");
        for (int i = 0; i < 50; i++)
            System.out.print("*");
        ;
        System.out.println("Please read instruction before starting to avoid any difficulties further");
        additional_info();

        System.out.println("Game starts !");
        stimulateMatch();

        result(teams[0], teams[1]);

        Summary();

    }
}