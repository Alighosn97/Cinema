package ma.emsi.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.emsi.cinema.dao.CategorieRepository;
import ma.emsi.cinema.dao.CinemaRepository;
import ma.emsi.cinema.dao.FilmRepository;
import ma.emsi.cinema.dao.PlaceRepository;
import ma.emsi.cinema.dao.ProjectionRepository;
import ma.emsi.cinema.dao.SalleRepository;
import ma.emsi.cinema.dao.SeanceRepository;
import ma.emsi.cinema.dao.TicketRepository;
import ma.emsi.cinema.dao.VilleRepository;
import ma.emsi.cinema.entities.Categorie;
import ma.emsi.cinema.entities.Cinema;
import ma.emsi.cinema.entities.Film;
import ma.emsi.cinema.entities.Place;
import ma.emsi.cinema.entities.Projection;
import ma.emsi.cinema.entities.Salle;
import ma.emsi.cinema.entities.Seance;
import ma.emsi.cinema.entities.Ticket;
import ma.emsi.cinema.entities.Ville;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService{
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	
	@Override
	public void initVilles() {
		Stream.of("Casablanca","Marrakesh","Rabat","Tanger").forEach(v->{
			Ville ville = new Ville();
			ville.setName(v);
			villeRepository.save(ville);
			
		});
		
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("MegaRama","IMAX","FOUNOUN","CHAHRAZAD","DAOULIZ").forEach(namecinema->{
				Cinema cinema = new Cinema();
				cinema.setName(namecinema);
				cinema.setVille(v);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinemaRepository.save(cinema);
			});
		});
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for (int i = 0; i < cinema.getNombreSalles(); i++) {
				Salle salle =new Salle();
				salle.setName("salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for (int i = 0; i < salle.getNombrePlace(); i++) {
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSeances() {
		DateFormat dateformat = new SimpleDateFormat("HH:mm");
		Stream.of("12:20","15:20","17:20","19:20","20:20").forEach(s->{
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateformat.parse(s));
				seanceRepository.save(seance);
			}catch(ParseException e) {
				e.printStackTrace();
			}
		});
		
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire","Drama","Fiction","Action").forEach(cat->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
			});
		
	}

	@Override
	public void initfilms() {
		List<Categorie> categories = categorieRepository.findAll();
		double[]duree = new double[] {1,1.5,2,2.5,3};
		Stream.of("Game of Thrones","SN","Spider man","IRON MAN","Cat women").forEach(film->{
		Film film1 = new Film();
		film1.setTitre(film);
		film1.setDuree(duree[new Random().nextInt(duree.length)]);
		film1.setPhoto(film.replaceAll(" ", ""));
		film1.setCategorie(categories.get(new Random().nextInt(categories.size())));
		filmRepository.save(film1);
		});
		
		
	}

	@Override
	public void initProjections() {
		double[] prices = new double[] {30,50,60,70,90,100};
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					filmRepository.findAll().forEach(film->{
						seanceRepository.findAll().forEach(seance->{
							Projection projection = new Projection();
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					});
				});
			});
		});
		
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
		
	}

}
