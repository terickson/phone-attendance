from fpdf import FPDF

pdf = FPDF()
pdf.set_font("Helvetica")
pdf.add_page()
for i in range(0, 24):
    val=str(i+1)
    pdf.interleaved2of5(val, x=40*(i%5), y=20*(int(i/5)), w=3, h=10)
    pdf.text(x=40*(i%5), y=20*(int(i/5))+14, txt=val)

pdf.output("interleaved2of5.pdf")

