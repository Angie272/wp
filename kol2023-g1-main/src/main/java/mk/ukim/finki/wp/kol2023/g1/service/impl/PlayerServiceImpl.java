package mk.ukim.finki.wp.kol2023.g1.service.impl;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.Team;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.repository.TeamRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import mk.ukim.finki.wp.kol2023.g1.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;

    public PlayerServiceImpl(PlayerRepository playerRepository,
                             TeamRepository teamRepository, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.teamService = teamService;
    }

    @Override
    public List<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);

    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Team team1 = teamService.findById(team);
        return playerRepository.save(new Player(name, bio, pointsPerGame, position, team1));
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Player player = findById(id);
        Team team1 = teamService.findById(team);
        player.setName(name);
        player.setBio(bio);
        player.setPointsPerGame(pointsPerGame);
        player.setPosition(position);
        player.setTeam(team1);
        return playerRepository.save(player);
    }

    @Override
    public Player delete(Long id) {
        Player player = findById(id);
        playerRepository.delete(player);
        return player;

    }

    @Override
    public Player vote(Long id) {
        Player player = findById(id);
        player.setVotes(player.getVotes() + 1);
        playerRepository.save(player);
        return player;
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {
        if (position == null && pointsPerGame == null) {
            return listAllPlayers();
        } else if (pointsPerGame == null) {
            return playerRepository.findAllByPosition(position);

        } else if (position == null) {
            return playerRepository.findAllByPointsPerGameIsLessThan(pointsPerGame);

        } else
            return playerRepository.findAllByPointsPerGameIsLessThanAndAndPosition(pointsPerGame, position);
    }
}
