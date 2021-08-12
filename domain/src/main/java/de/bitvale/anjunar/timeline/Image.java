package de.bitvale.anjunar.timeline;

import de.bitvale.common.filedisk.HarddiskFile;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "PostImage")
@Table(name = "do_image")
public class Image extends HarddiskFile { }
