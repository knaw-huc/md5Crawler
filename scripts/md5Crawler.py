# python2 script!
import hashlib
import glob
import argparse
import sys
import os
from datetime import datetime


def stderr(text):
    sys.stderr.write("{}\n".format(text))


def arguments():
    ap = argparse.ArgumentParser(description='Read all files in a directory and calculate md5 hash')
    ap.add_argument('-d', '--directory', default = ".")  
    args = vars(ap.parse_args())
    return args
    

def end_prog(code=0):
    stderr(datetime.today().strftime("einde: %H:%M:%S"))
    sys.exit(code)


if __name__ == "__main__":
    stderr(datetime.today().strftime("start: %H:%M:%S"))

    args = arguments()
    base = args['directory']
    if not os.path.exists(base):
        stderr("Could not find path: %s"%(base))
        end_prog(1)

    all_files = glob.glob(base)
    teller = 0
    for dirpath, dirs, files in os.walk(base):
        for filename in files:
	    fname = os.path.abspath(os.path.join(dirpath,filename))
            if not os.path.isdir(fname):
                try:
                    hash_res = hashlib.md5(open(fname,'rb').read()).hexdigest()
                    print('"{}","{}"'.format(fname, hash_res))
                    teller += 1
                except IOError:
                    stderr('File "{}" caused an IOError'.format(fname))
    stderr('Computed hash for {} files in {}'.format(teller,base))
    end_prog()
