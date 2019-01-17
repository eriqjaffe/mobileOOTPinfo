select a.player_id, b.first_name, b.last_name, a.sub_league_id, c.abbr, coalesce(e.abbr,'N/A'),
a.year, a.category, a.place, a.amount
from OTBA_players_league_leader a
inner join OTBA_players b
on a.player_id = b.player_id
inner join OTBA_sub_leagues c
on a.league_id = c.league_id  and a.sub_league_id = c.sub_league_id
inner join OTBA_players_career_batting_stats d
on a.player_id = d.player_id and d.split_id = 1 and d.year = 2017
left join OTBA_team_history e
on d.team_id = e.team_id and e.year = 2017
where a.league_id = 100 and a.year = 2017 and a.place = 1 and category = 0
group by a.year, a.category, a.sub_league_id, a.place