from fpdf import FPDF
import qrcode
import os

def main():
    baseImgLocation='/tmp/wrTest'
    pdf = FPDF()
    pdf.set_font("Helvetica")
    pdf.add_page()
    for i in range(0, 24):
        val=str(i+1)
        use_qr(pdf, baseImgLocation, i, val)
        #use_code39(pdf, i, val)
    pdf.output("interleaved2of5.pdf")

def use_qr(pdf, imgLocation, i, val):
    img = qrcode.make(val)
    imgLocation+=val
    imgLocation+='.png'
    img.save(imgLocation)
    pdf.image(imgLocation, x=40*(i%5), y=20*(int(i/5)), h=10)
    pdf.text(x=40*(i%5), y=20*(int(i/5))+14, txt=val)
    os.remove(imgLocation)

def use_code39(pdf, i, val):
    pdf.code39(val, x=40*(i%5), y=20*(int(i/5)), h=10)
    pdf.text(x=40*(i%5), y=20*(int(i/5))+14, txt=val)
main()
