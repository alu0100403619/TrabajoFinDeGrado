# -----------------------
# Proyecto Final de Grado
#  
#
# by Francisco de Sande <fsande@ull.es>
# date: 20.02.2015
# ---

LATEX    = latex
BIBTEX   = bibtex
PDFLATEX = pdflatex
L2HTML   = latex2html
DVIPS    = dvips
BASURA   = *.dvi *.bbl *.aux *.toc *.lof *.lot *.lol *.log *.blg *.out *~ *.pdf *.ps *.backup *.pdf.old

MASTER   = memoria-tfg.tex
SRC      = \
						Previos/Portada.tex \
						Previos/Certificado.tex \
						Previos/Agradecimientos.tex \
						Previos/Licencia.tex \
						Previos/Resumen.tex \
						Previos/Summary.tex \
						Capitulos/Cap1_Introduccion.tex \
						Capitulos/Cap2_EspecificacionRequisitos.tex \
						Capitulos/Cap3_Objetivos.tex \
						Capitulos/Cap4_Antecedentes.tex \
						Capitulos/Cap5_Problemas.tex \
						Capitulos/TODO.tex \
						Apendices/ApendiceA_DiagramaClases.tex \
						Apendices/ApendiceB_DiagramaActividades.tex \
						
DVI = $(MASTER:.tex=.dvi)
BIB = $(MASTER:.tex=.bib)
PS  = $(MASTER:.tex=.ps)
PDF = $(MASTER:.tex=.pdf)
MASTERSIN =  $(MASTER:.tex=)

all: 
	touch memoria-pfc.`date +%Y-%m-%d`.pdf
	$(MAKE) pdf
	mv memoria-pfc.`date +%Y-%m-%d`.pdf memoria-pfc.`date +%Y-%m-%d`.pdf.old
	cp memoria-pfc.pdf memoria-pfc.`date +%Y-%m-%d`.pdf

HTML: $(PDF)
	$(L2HTML) -dir HTML -split 4 -local_icons -long_titles 20 $(MASTER)

pdf: $(MASTER) $(SRC)
	$(PDFLATEX) $(MASTER) && $(MAKE) bib && $(PDFLATEX) $(MASTER) && $(PDFLATEX) $(MASTER)

ps: $(MASTER) $(SRC) $(DVI)
	$(DVIPS) -o $(PS) $(DVI)

bib: bibliografia/bibliografia.bib
	$(BIBTEX) $(MASTERSIN)

$(DVI): $(MASTER) $(SRC)
	$(LATEX) $(MASTER); $(MAKE) bib; $(LATEX) $(MASTER); $(LATEX) $(MASTER)   

clean:
	$(RM) *.dvi *.bbl *.aux *.toc *.lof *.lot *.log *.blg *.lol *.brf *~ *.out HTML/*

del:
	$(RM) -R $(BASURA);
	cd Bibliografia;    $(RM) -R $(BASURA);  \
	cd ../Capitulos;    $(RM) -R $(BASURA);  \
	cd ../Previos;       $(RM) -R $(BASURA);  \
	cd ../Apendices;       $(RM) -R $(BASURA);  \

mrproper:
	$(MAKE) delete

#.SUFFIXES: .tex .dvi. .ps .pdf
#
#%.dvi: %.tex
#	$(LATEX) $(SRC)
#
# end
# --
