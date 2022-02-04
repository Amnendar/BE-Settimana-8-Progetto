package it.contocorrente.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.contocorrente.entity.Contocorrente;
import it.contocorrente.entity.Movimento;
import it.contocorrente.entity.Operazione;


@Path("/conto")
public class ContocorrenteRest {

	private static ArrayList<Contocorrente> conti = new ArrayList<>();
	private static ArrayList<Movimento> movimenti = new ArrayList<>(); 

	//path index:
	//http://localhost:8080/EWallet/rest/conto


	//aggiunge un conto
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaConto(Contocorrente c) {
		for (Contocorrente conto : conti) {
			if(conto.getIban().equals(c.getIban())) {
				return Response.status(404).entity("ERRORE! Iban gia presente!").build();
			}
			
		}
		conti.add(c);

		return Response.status(200).entity("Creazione conto avvenuta con successo!").build();

	}

	//rimuove un conto
	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancellaConto(Contocorrente c) {
		for (Contocorrente con : conti) {
			if(con.getIban().equals(c.getIban())) {
				conti.remove(con);
				return Response.status(200).entity("Conto rimosso correttamente!").build();
			}

		}
		return Response.status(404).entity("Un conto con questo Iban non e presente!").build();

	}

	//aggiorna un conto
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response aggiornoConto(Contocorrente c) {
		for (Contocorrente con : conti) {
			if(con.getIban().equals(c.getIban())) {
				int index = conti.lastIndexOf(con);
				conti.set(index, c);
				return Response.status(200).entity("Conto aggiornato correttamente!").build();
			}
		}
		return Response.status(404).entity("Un conto con questo Iban non esiste!").build();
	}

	//effettua un'operazione
	@PUT
	@Path("/movimento")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response movimento(Movimento m) {
		for (Contocorrente con : conti) {
			if(con.getIban().equals(m.getIban())) {

				if (m.getTipo().equals(Operazione.PRELIEVO)) {
					if (m.getImporto() > con.getSaldo()) {
						return Response.status(406).entity("Non puoi prelevare piu del tuo saldo attuale").build();
					}
					if (m.getImporto() < 0) {
						return Response.status(405).entity("ERRORE! non puoi selezionare un importo negativo").build();
					}
					double nuovoSaldo = con.getSaldo()- m.getImporto();
					con.setSaldo(nuovoSaldo);
					movimenti.add(m);
					return Response.status(200).entity("Operazione effettuata! il nuovo saldo e: "+ nuovoSaldo ).build();
				}

				if (m.getTipo().equals(Operazione.VERSAMENTO)) {
					if(m.getImporto() < 0) {
						return Response.status(405).entity("Non puoi versare un importo negativo").build();
					}
					double nuovoSaldo = con.getSaldo() + m.getImporto();
					con.setSaldo(nuovoSaldo);
					movimenti.add(m);
					return Response.status(200).entity("Operazione effettuata! il nuovo saldo e: "+ nuovoSaldo ).build();
				}
			}
		}
		return Response.status(404).entity("ERRORE! Tipo operazione non valido").build();
	}


	//restituisce una lista di tutti i movimenti
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movimento> getMovimenti(){

		return movimenti;
	}

	//restituisce una lista di tutti i conti
	@GET
	@Path("/conti")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contocorrente> getConti(){

		return conti;
	}
	
	//restituisce i movimenti legati ad un singolo conto
	@GET
	@Path("/iban")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movimento> getContiIban(Contocorrente c){
		ArrayList<Movimento> movimentiIban = new ArrayList<>();
		for (Movimento mov : movimenti) {
			if(mov.getIban().equals(c.getIban())) {
				movimentiIban.add(mov);
			}
			
		}
		
		return movimentiIban;
	}
	

}
