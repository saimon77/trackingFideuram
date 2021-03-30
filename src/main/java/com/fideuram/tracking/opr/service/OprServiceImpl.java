package com.fideuram.tracking.opr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fideuram.aggiornauniverso.ws.UniversoWsService;
import com.fideuram.aggiornauniverso.ws.UniversoWsServiceService;
import com.fideuram.tracking.opr.dao.DaoOpr;
import com.fideuram.tracking.opr.dto.Agenda;
import com.fideuram.tracking.opr.dto.Beneficiario;
import com.fideuram.tracking.opr.dto.Elaborazione;
import com.fideuram.tracking.opr.dto.ElencoStati;
import com.fideuram.tracking.opr.dto.MapStringObj;
import com.fideuram.tracking.opr.dto.Operazione;
import com.fideuram.tracking.opr.dto.Pagamento;
import com.fideuram.tracking.opr.dto.ReferenteTerzo;
import com.fideuram.tracking.opr.dto.Soggetto;
import com.fideuram.tracking.opr.dto.TipoStato;
import com.fideuram.tracking.opr.restController.Puc001Controller;
import com.fideuram.tracking.opr.utility.PdfExecute;

import net.iharder.Base64;

@Service
public class OprServiceImpl implements OprService {

	public Logger logger = Logger.getLogger(OprServiceImpl.class);
	private final String PREVIDENZA = "PREVIDENZA";
	private final String VITA = "VITA";

	private static final List<Integer> inLogic = Arrays.asList(2, 3, 5, 15, 16, 17, 18, 19, 22, 23, 24, 27, 28, 29, 30,
			32, 33);

	@Autowired
	private DaoOpr dao;

	@Autowired
	private MailSender mailServ;

	@Autowired
	@Qualifier("getProperties")
	private Properties prop;

	@Autowired
	private UniversoService serviceUniverso;

	public List<Operazione> getUltimeElaborazioni(String polizza, String cf, String categoria, String sottoCategoria)
			throws Exception {
		List<Elaborazione> listElab = dao.getUltimeElaborazioni(polizza, cf, categoria, sottoCategoria);
		aggiornaPagamaneto(listElab);
		List<Operazione> listOpr = null;
		if (listElab != null && listElab.size() > 0) {
			inserisciAggiornate(listElab, cf);
			listOpr = mappingOpr(listElab);
		}

		aggiornaStatiUltima(listOpr);

		return listOpr;
	}

	private void aggiornaStatiUltima(List<Operazione> listOpr) throws Exception {
		if(listOpr==null)
			return;
		for (Operazione opr : listOpr) {
			for (String cat : opr.getCategorie().keySet()) {
				for (Elaborazione elab : opr.getCategorie().get(cat)) {
					String dataRic = null;
					if (elab.getDataRichiesta() != null)
						dataRic = new SimpleDateFormat("yyyy-MM-dd").format(elab.getDataRichiesta());
					String numPratica = elab.getNumeroPratica();
					if (inLogic.contains(opr.getTipoOperazioneTrackingID())) {
						numPratica = null;
					} else {
						dataRic = null;
					}
					List<Operazione> listOprDettaglio = getDettaglioElaborazione(elab.getContraente(),
							elab.getTipoCategoria(), elab.getTipoSottoCategoria(), elab.getPolizza(),
							numPratica, dataRic,elab.getTipoOperazioneTrackingID());
					if(listOprDettaglio==null)
						continue;
					for (Operazione oprDet : listOprDettaglio) {
						if (opr.getTipoOperazioneTrackingID() == oprDet.getTipoOperazioneTrackingID()) {
							for (String catDett : oprDet.getCategorie().keySet()) {
								for (Elaborazione elabDett : oprDet.getCategorie().get(catDett)) {
									if (elab.getElaborazioneID() == elabDett.getElaborazioneID()) {
										elab.setElencoStatiOperazione(elabDett.getElencoStatiOperazione());
										break;
									}
								}
								break;
							}
						}
						break;
					}

				}
			}
		}
	}

	private void aggiornaPagamaneto(List<Elaborazione> listElab) throws Exception {
		for (Elaborazione elaborazione : listElab) {
			if (elaborazione.getImportoRichiesto() != null) {
				logger.info("non aggiorno l'importo richiesto in quanto il campo è già valorizzato con "
						+ elaborazione.getImportoRichiesto());
				continue;
			}
			Pagamento pag = getPagamento(elaborazione.getContraente(), elaborazione.getPolizza(),
					elaborazione.getNumeroPratica());
			elaborazione.setImportoRichiesto(pag.getImportoRichiesto());
		}

	}

	private List<Operazione> mappingOpr(List<Elaborazione> listElab) throws Exception {
		logger.info(String.format("effettuo il mapping per %d elementi", listElab.size()));
		List<Operazione> listOpr = null;
		if (listElab != null && !listElab.isEmpty()) {
			Operazione opr = null;
			for (Elaborazione elab : listElab) {
				if (listOpr == null) {
					listOpr = new ArrayList<Operazione>();
					opr = new Operazione();
					opr.setOperazione(elab.getOperazione());
					opr.setTipoOperazioneTrackingID(elab.getTipoOperazioneTrackingID());
					addElab(opr, elab);
					listOpr.add(opr);
				} else if (notPresentOpr(elab, listOpr)) {
					opr = new Operazione();
					opr.setOperazione(elab.getOperazione());
					opr.setTipoOperazioneTrackingID(elab.getTipoOperazioneTrackingID());
					addElab(opr, elab);
					listOpr.add(opr);
				}
			}
		}

		if (listOpr != null) {

			List<ElencoStati> stati = dao.getElencoStati();
			// ordino per tipo operazione
			Collections.sort(listOpr, new Comparator<Operazione>() {

				@Override
				public int compare(Operazione o1, Operazione o2) {
					return o1.getTipoOperazioneTrackingID() - o2.getTipoOperazioneTrackingID();
				}
			});
			for (Operazione op : listOpr) {

				// ordina le elaborazioni per data richiesta
				for (String categoria : op.getCategorie().keySet()) {
					Collections.sort(op.getCategorie().get(categoria), new Comparator<Elaborazione>() {

						@Override
						public int compare(Elaborazione o1, Elaborazione o2) {
							if (o1.getDataEstrazione().equals(o2.getDataEstrazione()))
								return o1.getElaborazioneID() - o2.getElaborazioneID();
							return o1.getDataEstrazione().before(o2.getDataEstrazione()) == true ? 1 : -1;
						}
					});
				}

				// ordino e inserisco gli stati

//				aggiungiStatiOpr(op,stati);

//				for (ElencoStati Stati : stati) {
//					if ((op.getTipoOperazioneTrackingID() + "").equals(Stati.getIdOperazione())) {
//						Collections.sort(Stati.getListaStati(), new Comparator<TipoStato>() {
//
//							@Override
//							public int compare(TipoStato o1, TipoStato o2) {
//								return o1.getPosizione() - o2.getPosizione();
//							}
//						});
//						op.setElencoStatiOperazione(Stati.getListaStati());
//						break;
//					}
//				}
			}
			aggiungiStatiElab(listOpr, stati);
		}
		logger.info("Terminato mapping");
		return listOpr;
	}

//	private void aggiungiStatiOpr(Operazione op, List<ElencoStati> stati) {
//		// ordino e inserisco gli stati
//		for (ElencoStati elStati : stati) {
//			if ((op.getTipoOperazioneTrackingID() + "").equals(elStati.getIdOperazione())) {
//				Collections.sort(elStati.getListaStati(), new Comparator<TipoStato>() {
//
//					@Override
//					public int compare(TipoStato o1, TipoStato o2) {
//						return o1.getPosizione() - o2.getPosizione();
//					}
//				});
//				op.setElencoStatiOperazione(elStati.getListaStati());
//				break;
//			}
//		}
//	}

	private void aggiungiStatiElab(List<Operazione> listOpr, List<ElencoStati> stati) {

		for (Operazione op : listOpr) {
			for (String cat : op.getCategorie().keySet()) {
				for (Elaborazione elab : op.getCategorie().get(cat)) {
					for (ElencoStati elStati : stati) {
						if ((op.getTipoOperazioneTrackingID() + "").equals(elStati.getIdOperazione())) {
							Collections.sort(elStati.getListaStati(), new Comparator<TipoStato>() {

								@Override
								public int compare(TipoStato o1, TipoStato o2) {
									return o1.getPosizione() - o2.getPosizione();
								}
							});
							elab.setElencoStatiOperazione(elStati.getListaStati());
							break;
						}
					}

				}
			}

		}
		arricchisciUltimaElab(listOpr);
	}

	private void addElab(Operazione opr, Elaborazione elab) {
		Map<String, List<Elaborazione>> mapCat = opr.getCategorie();

		List<Elaborazione> listElab = null;
		if (mapCat.isEmpty()) {
			listElab = new ArrayList<Elaborazione>();
			listElab.add(elab);
			mapCat.put(getTipo(elab), listElab);
		} else {
			listElab = mapCat.get(getTipo(elab));
			if (listElab != null)
				listElab.add(elab);
			else {
				listElab = new ArrayList<Elaborazione>();
				listElab.add(elab);
				mapCat.put(getTipo(elab), listElab);
			}
		}
	}

	private String getTipo(Elaborazione elab) {
		if ("90".equals(elab.getIdProdotto()))
			return PREVIDENZA;
		return VITA;
	}

	private boolean notPresentOpr(Elaborazione elab, List<Operazione> listOpr) {
		boolean found = true;

		for (Operazione opr : listOpr) {
			if (opr.getTipoOperazioneTrackingID() == elab.getTipoOperazioneTrackingID()) {
				addElab(opr, elab);
				found = false;
				break;
			}

		}
		return found;
	}

	public List<Operazione> getDettaglioElaborazione(String cf, String categoria, String sottoCategoria, String polizza,
			String numeroPratica, String dataRichiesta,int tipoOperazioneTrackingID) throws Exception {
		logger.info(String.format("getDettaglio %s - %s - %s - %s - %s - %s", cf, categoria, sottoCategoria, polizza,
				numeroPratica, dataRichiesta));
		List<Elaborazione> listElab = dao.getDettaglioElaborazione(cf, categoria, sottoCategoria, polizza,
				numeroPratica, dataRichiesta,tipoOperazioneTrackingID);
		aggiornaPagamaneto(listElab);
		List<Operazione> listOpr = null;
		if (listElab != null && listElab.size() > 0) {
			inserisciAggiornate(listElab, cf);
			listOpr = mappingOpr(listElab);
			listOpr = arricchisciUltima(listOpr);
		}
		return listOpr;
	}

	public boolean updateLastView(String cf) throws Exception {
		return dao.updateLastView(cf);

	}

	private void inserisciAggiornate(List<Elaborazione> listElab, String cf) throws Exception {
		logger.info("inizio aggiornamento pratiche trovate per cf: " + cf);
		if (cf == null)
			return;
		Timestamp dateLastView = dao.getLastvieW(cf);
//		Timestamp time = dao.getLastvieW(cf);
		for (Elaborazione elab : listElab) {
			if (dateLastView == null)
				elab.setAggiornate(true);
			else {
				Date dateOpr = elab.getDataRichiesta();
//			java.util.Date dateLastView = Date.from(time.toInstant());
				if (dateOpr.after(dateLastView))
					elab.setAggiornate(true);
			}
		}
		logger.info("terminato aggiornamento pratiche trovate");
	}

	private List<Operazione> arricchisciUltima(List<Operazione> opr) {
		logger.info("inizio arricchimento ultima pratica");
		List<Operazione> resp = new ArrayList<Operazione>();
		if (opr != null) {
			for (Operazione op : opr) {

				// ordino la lista per mettere solo l'ultima elaborazione e poi aggiornare i
				// campi data per gli stati
				for (String tipo : op.getCategorie().keySet()) {

					Collections.sort(op.getCategorie().get(tipo), new Comparator<Elaborazione>() {

						public int compare(Elaborazione o1, Elaborazione o2) {
							return o1.getElaborazioneID() - o2.getElaborazioneID();
						}
					});

					List<Elaborazione> oldList = Arrays.asList(new Elaborazione[op.getCategorie().get(tipo).size()]);
					Collections.copy(oldList, op.getCategorie().get(tipo));
					List<Elaborazione> lista = op.getCategorie().get(tipo);
					lista.clear();
					lista.add(oldList.get(oldList.size() - 1));

					for (Elaborazione ela : oldList) {
						for (TipoStato stato : ela.getElencoStatiOperazione()) {
							if (stato.getId() == ela.getStatoID()) {
								stato.setData(ela.getDataEstrazione());
								stato.setInfo(ela.getDescrizioneStatoElaborazione());
								stato.setCanale(ela.getCanale());
								continue;
							}
						}
					}
					resp.add(op);
				}
			}
		}
		logger.info("terimnato arricchimento ultima pratica");
		return resp;

	}
//	private List<Operazione> arricchisciUltima(List<Operazione> opr) {
//		logger.info("inizio arricchimento ultima pratica");
//		List<Operazione> resp = new ArrayList<Operazione>();
//		if (opr != null) {
//			for (Operazione op : opr) {
//				
//				// ordino la lista per mettere solo l'ultima elaborazione e poi aggiornare i
//				// campi data per gli stati
//				for (String tipo : op.getCategorie().keySet()) {
//					
//					Collections.sort(op.getCategorie().get(tipo), new Comparator<Elaborazione>() {
//						
//						public int compare(Elaborazione o1, Elaborazione o2) {
//							return o1.getElaborazioneID() - o2.getElaborazioneID();
//						}
//					});
//					
//					List<Elaborazione> oldList = Arrays.asList(new Elaborazione[op.getCategorie().get(tipo).size()]);
//					Collections.copy(oldList, op.getCategorie().get(tipo));
//					List<Elaborazione> lista = op.getCategorie().get(tipo);
//					lista.clear();
//					lista.add(oldList.get(oldList.size() - 1));
//					
//					for (Elaborazione ela : oldList) {
//						for (TipoStato stato : op.getElencoStatiOperazione()) {
//							if (stato.getId() == ela.getStatoID()) {
//								stato.setData(ela.getDataEstrazione());
//								stato.setInfo(ela.getDescrizioneStatoElaborazione());
//								continue;
//							}
//						}
//					}
//					resp.add(op);
//				}
//			}
//		}
//		logger.info("terimnato arricchimento ultima pratica");
//		return resp;
//		
//	}

	private List<Operazione> arricchisciUltimaElab(List<Operazione> opr) {
		logger.info("inizio arricchimento ultima elab");
		List<Operazione> resp = new ArrayList<Operazione>();
		if (opr != null) {
			for (Operazione op : opr) {

				// ordino la lista per mettere solo l'ultima elaborazione e poi aggiornare i
				// campi data per gli stati
				for (String tipo : op.getCategorie().keySet()) {

					Collections.sort(op.getCategorie().get(tipo), new Comparator<Elaborazione>() {

						public int compare(Elaborazione o1, Elaborazione o2) {
							return o1.getElaborazioneID() - o2.getElaborazioneID();
						}
					});

//					List<Elaborazione> oldList = Arrays.asList(new Elaborazione[op.getCategorie().get(tipo).size()]);
//					Collections.copy(oldList, op.getCategorie().get(tipo));
					List<Elaborazione> lista = op.getCategorie().get(tipo);
//					lista.clear();
//					lista.add(oldList.get(oldList.size() - 1));

					for (Elaborazione ela : lista) {
						for (TipoStato stato : ela.getElencoStatiOperazione()) {
							if (stato.getId() == ela.getStatoID()) {
								stato.setData(ela.getDataEstrazione());
								stato.setInfo(ela.getDescrizioneStatoElaborazione());
								stato.setCanale(ela.getCanale());
								continue;
							}
						}
					}
					resp.add(op);
				}
			}
		}
		logger.info("terimnato arricchimento ultima pratica");
		return resp;

	}

	public boolean sentDoc(String cf, String polizza, String utente, MapStringObj filedata) throws Exception {

		Map<String, String> listDoc = filedata.getFiles();

		boolean done = false;
		File localFile = null;
		try {
			for (String nomeFile : listDoc.keySet()) {
				if (nomeFile == null) {
					String error = "un nome file passato risulta nullo";
					logger.error(error);
					throw new Exception(error);
				}
				if (!nomeFile.contains(".")) {
					String error = "il file " + nomeFile
							+ " non risulta formattato correttamente [nomeFile].[estensione]";
					logger.error(error);
					throw new Exception(error);
				}
				String[] nomeEst = nomeFile.split("\\.");
				byte[] docDecode = Base64.decode(listDoc.get(nomeFile));
				String estensione = "." + nomeEst[1];
				logger.info("nome del file: " + nomeFile);
				java.util.Date d = Calendar.getInstance().getTime();
				logger.info("data da aggiungere al nome file: " + d);
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy__HH_mm_ss");
				String nomeFileFtp = polizza + "_" + formatter.format(d) + "__";
				logger.info("nome file da inviare a ftp: " + nomeFileFtp);
				localFile = File.createTempFile(nomeFileFtp, estensione);
				FileOutputStream file = new FileOutputStream(localFile);
				file.write(docDecode);
				file.close();
//				prima registro su ftp
				done = sentFtp(localFile);
				boolean delete = localFile.delete();
				if (delete)
					logger.debug("file temp " + localFile.getName() + " cancellato correttamente");
				else
					logger.debug("file temp " + localFile.getName() + " NON cancellato correttamente");
//				ora registro il movimento sul db
				if (done) {
					String nomeFileInviato = nomeEst[0] + "." + nomeEst[1];
					dao.sentDoc(cf, polizza, utente, nomeFileInviato, nomeFileFtp);

				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			if (localFile.exists())
				localFile.delete();
		}

		return done;
	}

	private boolean sentFtp(File localFile) throws Exception {
		logger.info("inizio invio ftp per " + localFile);
		String portProp = prop.getProperty("ftp.port");
		int port = 21;
		if (portProp != null)
			port = Integer.parseInt(portProp);
		FTPUploader ftpServ = new FTPUploader(prop.getProperty("ftp.server"), prop.getProperty("ftp.user"),
				prop.getProperty("ftp.pwd"), port, prop.getProperty("ftp.path"));
		boolean done = ftpServ.sendFile(localFile);
		return done;

	}

	public Agenda getAgenda(List<Operazione> objResp, String cf) throws Exception {
		Agenda agenda = dao.getAgenda(cf);
		return agenda;
//		if (agenda == null)
//			return agenda;
//		boolean isMax = true;
////		controllo se c'è almeno un operazione con data aggiornamento maggiore per azzerare in caso l'agenda
//		oprElenco: for (Operazione opr : objResp) {
//			for (Elaborazione elab : opr.getListElaborazione()) {
//				if (elab.getDataRichiesta().after(agenda.getDataUltimoAccessp())) {
//					isMax = false;
//					break oprElenco;
//				}
//			}
//		}
//		if (isMax)
//			return agenda;
//		else
//			return null;
	}

	@Override
	public List<Beneficiario> listBen(String polizza) throws Exception {
		return dao.listBen(polizza);
	}

	@Override
	public List<ReferenteTerzo> listRefTer(String polizza) throws Exception {
		return dao.listRefTer(polizza);
	}

	@Override
	public boolean updateRef(ReferenteTerzo ref, String tipoModifica, String utente) throws Exception {
		boolean resp = dao.updateRef(ref, tipoModifica, utente);
		boolean respUniv = serviceUniverso.insertReferenteAmministrativo(ref, tipoModifica);
		logger.info("inserito referente su universo: " + respUniv);
		List<ReferenteTerzo> listRef = listRefTer(ref.getPolizzaID());
		if (listRef != null && !listRef.isEmpty()) {
			for (ReferenteTerzo referente : listRef) {
				if (referente.getCodFiscale() != ref.getCodFiscale()) {
					// invio mail
				}
			}
		}
		return resp;
	}

	@Override
	public boolean updateBen(Beneficiario ben, String tipoModifica, String utente) throws Exception {
		boolean resp = dao.updateBen(ben, tipoModifica, utente);
//		TODO chiamare il web service in base al tipoModifica
		List<Beneficiario> listBen = listBen(ben.getPolizzaID());
		if (listBen != null && !listBen.isEmpty()) {
			for (Beneficiario beneficiario : listBen) {
				if (beneficiario.getiD() != ben.getiD()) {
					// invio mail
				}
			}
		}
		return resp;
	}

	public Soggetto getSoggetto(String polizza, String tipo) {
		if (polizza == null || tipo == null)
			return null;
		Soggetto sogg = null;
		try {
			if ("assicurato".equals(tipo.toLowerCase()))
				sogg = dao.getAssicurato(polizza);
			else if ("contraente".equals(tipo.toLowerCase()))
				sogg = dao.getContraente(polizza);
		} catch (Exception e) {
			logger.error("errore nel recupero del soggetto " + tipo + " : " + e.getMessage());
		}
		return sogg;
	}

	private int getNumDocInviati(String cf, String polizza) throws Exception {
		return dao.geDocInviati(cf, polizza);
	}

	@Override
	public byte[] getFile(String polizza, String idFile) throws Exception {
		return PdfExecute.getPdfTest();
	}

	@Override
	public Pagamento getPagamento(String cf, String polizza, String numeroPratica) throws Exception {
		logger.info(String.format("arricchisco il pagamentoRichiesto con cf(%s) polizza(%s) numPratica(%s)", cf,
				polizza, numeroPratica));
		if (cf != null && polizza != null && numeroPratica != null) {
			Pagamento pag = dao.getPagamento(cf, polizza, numeroPratica);
			logger.info(String.format("importo trovato: %s", pag.getImportoRichiesto()));
			return pag;
		} else {
			logger.info(String
					.format("pagamento richieso non ricercato in quanto almeno uno dei campi chiave non è presente"));
		}
		return new Pagamento();
	}

}
