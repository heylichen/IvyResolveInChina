package com.heylichen.ivy;

import lombok.extern.slf4j.Slf4j;
import org.apache.ivy.Ivy;
import org.apache.ivy.core.IvyContext;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveEngine;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.repository.url.URLRepository;
import org.apache.ivy.plugins.resolver.IBiblioResolver;
import org.apache.ivy.util.DefaultMessageLogger;
import org.apache.ivy.util.Message;
import org.apache.ivy.util.url.URLHandlerRegistry;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ResolverTest {

    private URL IVY_SETTINGS;

    @Before
    public void setUp() throws Exception {
        IVY_SETTINGS = FileUtil.toURL("/ivy-settings.xml");
    }

    @Test
    public void engineTestAll() {
        String ivyFilesDirPath = "/ivy";
        File dir = FileUtil.toFile(ivyFilesDirPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".xml") && !name.contains("settings"));
        resolveIvyFile(Arrays.asList(files));
    }

    @Test
    public void engineTestOne() {
        //run this method to check resolve error in specific ivy.xml
        File dir = FileUtil.toFile("/ivy/ivy.xml");
        resolveIvyFile(Arrays.asList(dir));
    }

    private void resolveIvyFile(final List<File> files) {
        URLHandlerRegistry.setDefault(new BasicURLHandler2());
        Ivy ivy = Ivy.newInstance();
        ivy.setVariable("ivy.exclude.types", "source|javadoc");
        ivy.getLoggerEngine().setDefaultLogger(new DefaultMessageLogger(Message.MSG_VERBOSE));

        ivy.execute(new Ivy.IvyCallback() {
            public Object doInIvyContext(Ivy ivy, IvyContext context) {
                try {
                    // obviously we can use regular Ivy methods in the callback
                    ivy.configure(IVY_SETTINGS);
                    // and we can safely use Ivy engines too
                    for (File ivyFile : files) {
                        log.info("-------------> {}", ivyFile.getName());
                        ResolveEngine resolveEngine = ivy.getResolveEngine();

                        IvySettings settings = (IvySettings) resolveEngine.getSettings();
                        Collection resolverCollection = settings.getResolvers();
                        for (Object obj : resolverCollection) {
                            if (obj instanceof IBiblioResolver) {
                                IBiblioResolver iBiblioResolver = (IBiblioResolver) obj;
                                URLRepository2 repo = new URLRepository2();
                                URLRepository oldRepo = (URLRepository) iBiblioResolver.getRepository();
                                repo.setName(oldRepo.getName());
                                iBiblioResolver.setRepository(repo);
                            }
                        }

                        ResolveReport rp = resolveEngine.resolve(ivyFile.toURI().toURL());
                        if (rp.hasError()) {
                            log.info("has error to resolve {}", ivyFile.toURI().toURL());
                            break;
                        }

                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        });
    }

}
