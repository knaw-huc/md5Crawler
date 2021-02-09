# python2 script!
import glob
import hashlib
import argparse
from datetime import datetime
import csv
import os
import re
import sys

def stderr(text):
    sys.stderr.write("{}\n".format(text))


def arguments():
    ap = argparse.ArgumentParser(description='Read all files in a directory and calculate md5 hash')
    ap.add_argument('-f', '--listfile', default = ".")  
    ap.add_argument('-g', '--grens', default = 1000)  
    args = vars(ap.parse_args())
    return args
 

def start_calc(base, short_name):
    all_files = glob.glob(base)
    teller = 0
    output_file = "data/{}_md5_hash.csv".format(short_name)
    if os.path.exists(output_file):
	return
    output = open(output_file,"w")
    for dirpath, dirs, files in os.walk(base):
        for filename in files:
	    fname = os.path.abspath(os.path.join(dirpath,filename))
            if not os.path.isdir(fname):
                try:
                    hash_res = hashlib.md5(open(fname,'rb').read()).hexdigest()
                    output.write('"{}","{}"\n'.format(fname, hash_res))
                    teller += 1
                except IOError:
                    stderr('File "{}" caused an IOError'.format(fname))
    stderr('Computed hash for {} files in {}'.format(teller,base))


def end_prog(code=0):
    stderr(datetime.today().strftime("einde: %Y-%m-%d  %H:%M:%S"))
    sys.exit(code)


if __name__ == "__main__":
    stderr(datetime.today().strftime("start: %Y-%m-%d  %H:%M:%S"))

    args = arguments()
    listfile = args['listfile']
    grens = int(args['grens'])

    all_dirs = []
    with open(listfile, 'rb') as csvfile:
        spamreader = csv.reader(csvfile, delimiter=',', quotechar='"')
        for row in spamreader:
            try:
                row[0] = int(re.sub(r"\.","",row[0]))
                all_dirs.append(row)
            except ValueError:
                # headerrow
                stderr("ignored: {} (headerrow?)".format(row))

    res_list = sorted(all_dirs, key=lambda row: (row[0], row[1]))
    for row in res_list:
        base = "/data2/DigitaleOnderzoekscollecties/{}".format(row[1])
        if(not os.path.exists("data/{}_md5_hash.csv".format(row[1]))):
            if not os.path.exists(base):
                stderr("{} niet gevonden".format(base))
            else:
                if(row[0]<grens):
    		    stderr(datetime.today().strftime("%H:%M:%S"))
                    stderr("{} ({})".format(row[1],row[0]))
                    start_calc(base, row[1])
                else:
                    stderr("Te groot: {} ({})".format(row[1],row[0]))

    end_prog()
 
