from fpdf import FPDF
import qrcode
import os

def main():
    baseImgLocation='/tmp/wrTest'
    pdf = FPDF()
    pdf.set_font("Helvetica")
    qcp = 0
    for idx in range(0, 24):
        if qcp == 0:
            pdf.add_page()
        val=str(idx+1)
        use_qr(pdf, baseImgLocation, qcp, val)
        if int(val)%4 == 0:
            qcp = 0
        else:
            qcp+=1
    pdf.output("barcode.pdf")

def use_qr(pdf, imgLocation, i, val):
    offset=2
    img = qrcode.make(val)
    imgLocation+=val
    imgLocation+='.png'
    img.save(imgLocation)
    xval = 110*(i%offset)
    yval = 120*int((i/offset))
    pdf.text(x=xval+50, y=yval+5, txt=val)
    pdf.image(imgLocation, x=xval, y=yval+5, h=100)
    os.remove(imgLocation)

def use_code39(pdf, i, val):
    pdf.code39(val, x=40*(i%5), y=20*(int(i/5)), h=10)
    pdf.text(x=40*(i%5), y=20*(int(i/5))+14, txt=val)
main()
